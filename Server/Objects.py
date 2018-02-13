"""
    Пакет для хранения перечислений и классов
"""

from enum import Enum
import json


class ApiMethod(Enum):
    Authorization = "Auth"
    Registration = "Reg"
    GetListContracts = "Get_list_contract"


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


class Object:
    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)
