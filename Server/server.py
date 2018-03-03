from Server.ConnectDB import connect_database
from Server.Auth import authorization
from Server.Registration import registration
from Server.Registration import check_phone
from Server.Get_List_Contracts import get_list_contracts
from Server.Get_List_Contracts import get_contract_client
from Server.Get_List_Contracts import get_contract_witcher
from Server.Profile import get_profile
from Server.Profile import update_profile
from Server.Profile import write_comment_profile
from Server.Profile import exit_profile
from Server.Advert import create_advert
from Server.Advert import edit_advert
from Server.Advert import delete_advert
from Server.Advert import get_advert
from Server.Advert import add_witcher_in_contract
from Server.Advert import get_profile_desired_contract
from Server.Advert import write_comment_contract
from Server.Witcher import select_witcher
from Server.Witcher import answer_witcher
from Server.Witcher import refuse_contract
from Server.Witcher import contract_complited
from Server.Comments import get_list_comment
from Server.Town import get_towns
from Server.SecurityServer import security_requests
from http.server import BaseHTTPRequestHandler
from http.server import HTTPServer
from urllib.parse import parse_qsl, urlsplit
from cgi import parse_header
from cgi import parse_multipart
import json
from urllib.parse import parse_qs
from Server.Objects import Object
from Server.Objects import Status
from Server.Objects import ApiMethod

api_methods_get, api_methods_post = {}, {}
api_methods_post[ApiMethod.Authorization.value] = authorization
api_methods_post[ApiMethod.Registration.value] = registration
api_methods_post[ApiMethod.EditProfile.value] = update_profile
api_methods_post[ApiMethod.AddCommentProfile.value] = write_comment_profile
api_methods_post[ApiMethod.CreateAdvert.value] = create_advert
api_methods_post[ApiMethod.EditAdvert.value] = edit_advert
api_methods_post[ApiMethod.AddWitcherInContract.value] = add_witcher_in_contract
api_methods_post[ApiMethod.AddCommentContract.value] = write_comment_contract
api_methods_post[ApiMethod.SelectWitcherInContract.value] = select_witcher
api_methods_post[ApiMethod.AnswerWitcherInContract.value] = answer_witcher
api_methods_post[ApiMethod.RefuseContract.value] = refuse_contract
api_methods_post[ApiMethod.CheckPhone.value] = check_phone
api_methods_get[ApiMethod.GetAdvert.value] = get_advert
api_methods_get[ApiMethod.GetWitcherDesiredContract.value] = get_profile_desired_contract
api_methods_get[ApiMethod.DeleteAdvert.value] = delete_advert
api_methods_get[ApiMethod.GetListContracts.value] = get_list_contracts
api_methods_get[ApiMethod.GetProfile.value] = get_profile
api_methods_get[ApiMethod.GetListComments.value] = get_list_comment
api_methods_get[ApiMethod.GetTowns.value] = get_towns
api_methods_get[ApiMethod.GetContractClient.value] = get_contract_client
api_methods_get[ApiMethod.GetContractWitcher.value] = get_contract_witcher
api_methods_get[ApiMethod.ContractComplited.value] = contract_complited
api_methods_get[ApiMethod.ExitProfile.value] = exit_profile


class HttpServer(BaseHTTPRequestHandler):
    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/plain')
        self.end_headers()

    def do_GET(self):
        self._set_headers()
        url = self.requestline[9:-9]
        dct = dict(parse_qsl(urlsplit(url).path))
        if dct.get('id', None) is not None:
            dct['id'] = int(dct['id'])

        mymethod = dct.get('method', None)
        is_ok, stat = security_requests(mymethod, dct)
        if is_ok:
            cursor, connect = connect_database()
            value = api_methods_get[mymethod](cursor, dct)
            self.wfile.write(str.encode(value))
            print(value)
            cursor.close()
            connect.close()
        else:
            self.wfile.write(str.encode(stat))

    def do_HEAD(self):
        self._set_headers()

    def do_POST(self):
        self._set_headers()
        postvars = self.parse_POST()
        for key, val in postvars.items():
            mstr = key + val[0]
        dct = json.loads(mstr)

        mymethod = self.requestline[10:-9]
        is_ok, stat = security_requests(mymethod, dct, 0)
        if is_ok:
            cursor, connect = connect_database()
            value = api_methods_post[mymethod](cursor, dct)
            self.wfile.write(str.encode(value))
            print(value)
            cursor.close()
            connect.close()
        else:
            self.wfile.write(str.encode(stat))

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
