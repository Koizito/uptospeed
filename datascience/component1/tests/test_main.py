import ast

from component1.api import app


def test_tokenization():
    response = app.test_client().post("/tokenization",
                                      json={"string": "My name's Daniel Pereira, and I'm the fastest man alive!"})
    assert response.data.decode(
        "utf-8").strip() == '["My","name","\'s","Daniel","Pereira",",","and","I","\'m","the","fastest","man","alive","!"]'


def test_stopwords():
    response = app.test_client().post("/stopwords",
                                      json={"string": "My name's Daniel Pereira, and I'm the fastest man alive!"})
    assert response.data.decode("utf-8").strip() == '["name","\'s","and","\'m","the"]'


def test_postagging():
    response = app.test_client().post("/postagging",
                                      json={"string": "My name's Daniel Pereira, and I'm the fastest man alive!"})
    assert ast.literal_eval(response.data.decode("utf-8")) == {'My': 'PRON', 'name': 'NOUN', "'s": 'PART',
                                                               'Daniel': 'PROPN', 'Pereira': 'PROPN', ',': 'PUNCT',
                                                               'and': 'CCONJ', 'I': 'PRON', "'m": 'AUX', 'the': 'DET',
                                                               'fastest': 'ADJ', 'man': 'NOUN', 'alive': 'ADJ',
                                                               '!': 'PUNCT'}


def test_dependency():
    response = app.test_client().post("/dependency", json={
        "string": "My name's Daniel Pereira, and I'm the fastest man alive!"})
    with open('test_dependency.xml', 'r') as f:
        expected = f.read()

    actual = response.data.decode("utf-8")
    actual_id = actual.split('id="', 1)[1].split('-')[0]

    expected = expected.replace("4312f629dfa2479da4783017797a6121", actual_id)
    assert response.data.decode("utf-8") == expected


def test_ner():
    response = app.test_client().post("/ner", json={
        "string": "My name's Daniel Pereira, and I'm the fastest man alive!"})

    assert ast.literal_eval(response.data.decode("utf-8")) == {"Daniel Pereira": "PERSON"}


def test_mostsimilar():
    response = app.test_client().post("/mostsimilar",
                                      json={"string": "My name's Daniel Pereira, and I'm the fastest man alive!"})

    assert ast.literal_eval(response.data.decode("utf-8")) == {"'m": "ve", "'s": "his", "Daniel": "david", "I": "ive",
                                                               "My": "me", "Pereira": "carvalho", "alive": "dying",
                                                               "and": "both", "fastest": "quickest", "man": "woman",
                                                               "name": "named", "the": "of"}
