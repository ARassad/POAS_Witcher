"""
    Пакет для хранения перечислений и классов
"""

from enum import Enum
import json


class ApiMethod(Enum):
    Authorization = "Auth"
    Registration = "Reg"
    GetListContracts = "Get_list_contract"
    GetProfile = "GetProfile"
    EditProfile = "EditProfile"
    AddCommentProfile = "AddCommentProfile"
    CreateAdvert = "CreateAdvert"
    EditAdvert = "EditAdvert"
    DeleteAdvert = "DeleteAdvert"
    GetAdvert = "GetAdvert"
    AddWitcherInContract = "AddWitcherInContract"
    GetWitcherDesiredContract = "GetWitcherDesiredContract"
    AddCommentContract = "AddCommentContract"
    SelectWitcherInContract = "SelectWitcherInContract"
    AnswerWitcherInContract = "AnswerWitcherInContract"
    RefuseContract = "RefuseContract"
    GetListComments = "GetListComments"
    GetTowns = "GetTowns"
    GetContractClient = "GetContractClient"
    GetContractWitcher = "GetContractWitcher"


class Comment(Enum):
    TextComment = "text"


class ErrorMessage(Enum):
    EmptyRequest = "EmptyRequest"
    UnknownRequest = "UnknownRequest"
    NotAuth = "NotAuthorize"


class Status(Enum):
    Ok = "OK"
    Error = "Error"


class User(Enum):
    Login = "login"
    Password = "password"
    IsWitcher = "witcher"
    Token = "token"


class Object:
    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)
