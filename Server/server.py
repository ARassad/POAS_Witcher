from Server.ConnectDB import connect_database
from Server.Auth import authorization
from Server.Auth import authUser
from Server.Registration import registration
from Server.Get_List_Contracts import get_list_contracts
from Server.Profile import get_profile
from Server.Profile import update_profile
from Server.Profile import write_comment_profile
from http.server import BaseHTTPRequestHandler
from http.server import HTTPServer
from cgi import parse_header
from cgi import parse_multipart
import json
from urllib.parse import parse_qs
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
        elif authUser(cursor, dct):
            if mymethod == ApiMethod.GetListContracts.value:
                self.wfile.write(str.encode(get_list_contracts(cursor, dct)))
            elif mymethod == ApiMethod.GetProfile.value:
                dct['id'] = int(dct['id'])
                self.wfile.write(str.encode(get_profile(cursor, dct)))
            else:
                HttpServer.error_request(self, ErrorMessage.UnknownRequest.value)
        else:
            HttpServer.error_request(self, ErrorMessage.NotAuth.value)

    def do_HEAD(self):
        self._set_headers()

    def do_POST(self):
        self._set_headers()
        postvars = self.parse_POST()
        for key, val in postvars.items():
            mstr = key
        dct = json.loads(mstr)
        mymethod = self.requestline[10:-9]

        if mymethod is None:
            HttpServer.error_request(self, ErrorMessage.EmptyRequest.value)
        elif mymethod == ApiMethod.Authorization.value:
            self.wfile.write(str.encode(authorization(cursor, dct)))
        elif mymethod == ApiMethod.Registration.value:
            self.wfile.write(str.encode(registration(cursor, dct)))
        elif authUser(cursor, dct):
            if mymethod == ApiMethod.EditProfile.value:
                self.wfile.write(str.encode(update_profile(cursor, dct)))
            elif mymethod == ApiMethod.AddCommentProfile.value:
                self.wfile.write(str.encode(write_comment_profile(cursor, dct)))
            else:
                HttpServer.error_request(self, ErrorMessage.UnknownRequest.value)
        else:
            HttpServer.error_request(self, ErrorMessage.NotAuth.value)

    def parse_POST(self):
        ctype, pdict = parse_header(self.headers['content-type'])
        if ctype == 'multipart/form-data':
            postvars = parse_multipart(self.rfile, pdict)
        elif ctype == 'application/x-www-form-urlencoded':
            length = int(self.headers['content-length'])
            s = self.rfile.read(length)
            s = s.decode()
            postvars = parse_qs(
                s,
                keep_blank_values=1)
        else:
            postvars = {}
        return postvars

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
