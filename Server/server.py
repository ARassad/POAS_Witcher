from Server.ConnectDB import connect_database
from Server.Auth import authorization
from Server.Registration import registration
from Server.Get_List_Contracts import get_list_contracts
from Server.Profile import get_profile
from Server.Profile import update_profile
from Server.Profile import write_comment_profile
from Server.Advert import create_advert
from Server.Advert import edit_advert
from Server.Advert import delete_advert
from Server.Advert import get_advert
from Server.Advert import add_witcher_in_contract
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

api_methods_get, api_methods_post = {}, {}
api_methods_post[ApiMethod.Authorization.value] = authorization
api_methods_post[ApiMethod.Registration.value] = registration
api_methods_get[ApiMethod.GetListContracts.value] = get_list_contracts
api_methods_get[ApiMethod.GetProfile.value] = get_profile
api_methods_post[ApiMethod.EditProfile.value] = update_profile
api_methods_post[ApiMethod.AddCommentProfile.value] = write_comment_profile
api_methods_post[ApiMethod.CreateAdvert.value] = create_advert
api_methods_post[ApiMethod.EditAdvert.value] = edit_advert
api_methods_get[ApiMethod.DeleteAdvert.value] = delete_advert
api_methods_get[ApiMethod.GetAdvert.value] = get_advert
api_methods_post[ApiMethod.AddWitcherInContract.value] = add_witcher_in_contract


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
        elif api_methods_get.get(mymethod, None) is not None:
            if dct.get('id', None) is not None:
                dct['id'] = int(dct['id'])

            self.wfile.write(str.encode(api_methods_get[mymethod](cursor, dct)))
        else:
            HttpServer.error_request(self, ErrorMessage.UnknownRequest.value)

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
        elif api_methods_post.get(mymethod, None) is not None:
            self.wfile.write(str.encode(api_methods_post[mymethod](cursor, dct)))
        else:
            HttpServer.error_request(self, ErrorMessage.UnknownRequest.value)

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
