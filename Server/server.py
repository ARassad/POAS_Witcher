from Server.ConnectDB import connect_database
from Server.Auth import authorization
from Server.Registration import registration
from http.server import BaseHTTPRequestHandler, HTTPServer
from Server.Objects import Object
from Server.Objects import Status
from Server.Objects import ErrorMessage
from Server.Objects import ApiMethod

cursor = connect_database()


class HttpServer(BaseHTTPRequestHandler):
    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/plain')
        self.end_headers()

    def do_GET(self):
        self._set_headers()
        url = self.requestline[5 + 4:-9].split('&')
        dct = {}
        if url[0] != '' and url[0] != 'con.ico':
            dct = dict((val.split('=')[0], val.split('=')[1]) for val in url)
        mymethod = dct.get('method', None)

        if mymethod is None:
            HttpServer.error_request(self, ErrorMessage.EmptyRequest.value)
        elif mymethod == ApiMethod.Authorization.value:
            self.wfile.write(str.encode(authorization(cursor, dct)))
        elif mymethod == ApiMethod.Registration.value:
            self.wfile.write(str.encode(registration(cursor, dct)))
        else:
            HttpServer.error_request(self, ErrorMessage.UnknownRequest.value)


    def do_HEAD(self):
        self._set_headers()

    def do_POST(self):
        # Doesn't do anything with posted data
        self._set_headers()
        self.wfile.write(b"<html><body><h1>POST!</h1></body></html>")

    def error_request(self, message):
        obj = Object()
        obj.status = Status.Error.value
        obj.message = message
        self.wfile.write(str.encode(obj.toJSON()))


def run_httpserver(server_class=HTTPServer, handler_class=HttpServer, port=80):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print('Start')
    httpd.serve_forever()


if __name__ == "__main__":
    run_httpserver()
