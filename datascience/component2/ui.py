import argparse
import logging
import sys

import requests


def string_analyzer(args):
    logging.basicConfig(level=logging.INFO)
    logging.info(' String to analyse: ' + args.string)
    logging.info(' Kind of analysis to apply to the string: ' + args.function)
    print("#######################")

    response = requests.post(url="http://localhost:8080/" + args.function, json={'string': args.string})

    final_result = response.text.strip()

    if args.function == 'dependency':
        with open('svg_result.html', 'w') as file:
            file.write(final_result)
    else:
        print(final_result)
        print("#######################")


def parse_args(args_to_parse):
    parser = argparse.ArgumentParser()

    parser.add_argument('string', type=str, help='String to analyse')
    parser.add_argument('function', type=str, help='Kind of analysis to apply to the string')

    return parser.parse_args(args_to_parse)


if __name__ == "__main__":
    received_args = parse_args(sys.argv[1:])
    string_analyzer(received_args)
