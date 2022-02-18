FROM python:3.8-slim-buster

WORKDIR /component1

COPY ./ ./

RUN pip3 install -r requirements.txt

RUN python3 -m spacy download en_core_web_lg

ENV FLASK_APP api

CMD ["flask", "run", "--host=0.0.0.0"]

#docker build --no-cache --progress=plain -t datascience/component1:latest .

#docker run -d -p 8080:5000 datascience/component1