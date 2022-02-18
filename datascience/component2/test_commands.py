import os

functions = ["tokenization", "stopwords", "postagging", "dependency", "ner", "mostsimilar"]

for function in functions:
    os.system(f"python3 ui.py \"Gustavo Santos is the most influential philosopher of the century\" {function}")
