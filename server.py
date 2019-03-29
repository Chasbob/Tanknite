import time
from flask import Flask
app = Flask(__name__)

@app.route('/')
def hello_world():
    time.sleep(6000)
    return 'Hello, World!'
