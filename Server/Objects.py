"""
    Пакет для хранения перечислений и классов
"""

from enum import Enum
import json


class ApiMethod(Enum):
    Authorization = "Auth"


class ErrorMessage(Enum):
    EmptyRequest = "EmptyRequest"
    UnknownRequest = "UnknownRequest"


class Status(Enum):
    Ok = "OK"
    Error = "Error"


class EventAuth(Enum):
    SuccessAuthorizaion = "Success"
    LoginNotExist = "LoginNotExist"
    PasswordIncorrect = "IncorrectPasswd"


class User(Enum):
    Login = "login"
    Password = "password"


class Object:
    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, sort_keys=True, indent=4)