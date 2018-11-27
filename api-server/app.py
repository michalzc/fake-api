import logging
import random
import time
import uuid

import jsonpickle
from flask import Flask

from model import Item

logging.root.setLevel(logging.INFO)
app = Flask(__name__)


def get_items(item_id=None):
    item_id = item_id and item_id or str(uuid.uuid4())
    item = Item(item_id, "Description of %s" % str(item_id))
    r = round(random.random() * 10)
    app.logger.info("Request for %s - waiting %d seconds" % (item_id, r))
    time.sleep(r)
    app.logger.info("Returning item")
    return item

@app.route("/")
def hello():
    return "Hello World!"


@app.route("/items/<item_id>")
def handle_items(item_id):
    response = app.response_class(
        response=jsonpickle.dumps(get_items(item_id)),
        status=200,
        mimetype='application/json'
    )

    return response
