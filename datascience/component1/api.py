import logging
import re

import numpy as np
import spacy
from flask import Flask
from flask import jsonify
from flask import request

app = Flask(__name__)

app.logger.setLevel(logging.INFO)


@app.route("/tokenization", methods=['GET', 'POST'])
def tokenization():
    if request.method == 'POST':
        sentence = request.get_json()['string']
        app.logger.info("Doing the tokenization of the following string: \"%s\"", sentence)
        nlp = spacy.load("en_core_web_lg")
        doc = nlp(sentence)
        result = [token.text for token in doc]
        return jsonify(result)


@app.route("/stopwords", methods=['GET', 'POST'])
def stopwords():
    if request.method == 'POST':
        sentence = request.get_json()['string']
        app.logger.info("Finding the stop words in the following string: \"%s\"", sentence)
        nlp = spacy.load("en_core_web_lg")
        all_stopwords = nlp.Defaults.stop_words
        doc = nlp(sentence)
        sw = [token.text for token in doc if token.text in all_stopwords]
        return jsonify(sw)


@app.route("/postagging", methods=['GET', 'POST'])
def postagging():
    if request.method == 'POST':
        sentence = request.get_json()['string']
        app.logger.info("Doing the POS tagging of the following string: \"%s\"", sentence)
        nlp = spacy.load("en_core_web_lg")
        doc = nlp(sentence)
        result = {}

        for token in doc:
            result[token.text] = token.pos_

        return jsonify(result)


@app.route("/dependency", methods=['GET', 'POST'])
def dependency():
    if request.method == 'POST':
        sentence = request.get_json()['string']
        app.logger.info("Finding dependencies in the following string: \"%s\"", sentence)
        nlp = spacy.load("en_core_web_lg")
        doc = nlp(sentence)
        result = spacy.displacy.render(doc, style="dep")

        return result


@app.route("/ner", methods=['GET', 'POST'])
def ner():
    if request.method == 'POST':
        sentence = request.get_json()['string']
        app.logger.info("Doing named-entity recognition on the following string: \"%s\"", sentence)
        nlp = spacy.load("en_core_web_lg")
        doc = nlp(sentence)
        result = {}
        for ent in doc.ents:
            result[ent.text] = ent.label_

        return jsonify(result)


@app.route("/mostsimilar", methods=['GET', 'POST'])
def mostsimilar():
    if request.method == 'POST':
        sentence = request.get_json()['string']
        app.logger.info("Finding the most similar words for the ones in the following string: \"%s\"", sentence)
        nlp = spacy.load('en_core_web_lg')
        results = {}

        doc = nlp(sentence)

        for token in doc:
            if not token.is_punct:
                string = token.text

                ms = nlp.vocab.vectors.most_similar(
                    np.asarray([nlp.vocab.vectors[nlp.vocab.strings[string]]]), n=10)
                words = [nlp.vocab.strings[w] for w in ms[0][0]]

                words = [each_word.lower() for each_word in words]

                clean_words = []
                for word in words:
                    clean_words.append(re.sub(r'[^A-Za-z0-9]+', '', word))

                clean_words = list(dict.fromkeys(clean_words))

                if clean_words[1] != (string + 's'):
                    results[string] = clean_words[1]
                else:
                    results[string] = clean_words[2]

        return jsonify(results)
