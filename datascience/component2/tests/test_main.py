import io
import sys
from argparse import Namespace

from ..ui import string_analyzer


def test_ui(requests_mock):
    requests_mock.post('http://localhost:8080/tokenization', text='success')
    test_dict = dict({'function': 'tokenization', 'string': "My name's Daniel Pereira, and I'm the fastest man alive!"})
    ns = Namespace(**test_dict)
    captured_output = io.StringIO()
    sys.stdout = captured_output
    string_analyzer(ns)
    sys.stdout = sys.__stdout__
    assert captured_output.getvalue().strip() == "success"
