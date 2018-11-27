import logging
import random
import time
import uuid

import jsonpickle
from flask import Flask, request

from model import Item

logging.root.setLevel(logging.INFO)
app = Flask(__name__)


def get_items(item_id=None):
    item_id = item_id and item_id or str(uuid.uuid4())
    item = Item(item_id, "Description of %s" % str(item_id))
    r = round(random.random() * 4)
    app.logger.info("Request for %s - waiting %d seconds" % (item_id, r))
    time.sleep(r)
    app.logger.info("Returning item")
    return item


@app.route("/")
def hello():
    return "Hello World!"


@app.route("/items/<item_id>", methods=['GET'])
def handle_items(item_id):

    response = app.response_class(
        response=jsonpickle.dumps(get_items(item_id)),
        status=200,
        mimetype='application/json'
    )

    return response


@app.route("/items/<item_id>", methods=['PUT'])
def handle_put(item_id):
    # app.logger.info("Received:\nContentType: %s", request.content_type)
    r = round(random.random() * 4)
    app.logger.info("Request with id: %s and version %s waiting %d seconds" % (item_id, request.args.get('version'), r))
    time.sleep(r)
    app.logger.info("Returning item")

    return app.response_class(
        status=201,
        mimetype='application/json',
        response='{"success":true}'
    )
