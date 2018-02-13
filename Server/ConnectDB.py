import pyodbc

"""
    Функция присоединения к БД на azure
"""


def connect_database():
    server = 'poaswitcher.database.windows.net'
    database = 'witcher'
    username = 'Nordto'
    password = 'p0@sgovno'
    driver = '{ODBC Driver 13 for SQL Server}'
    cnxn = pyodbc.connect('DRIVER={};PORT=1433;SERVER={};PORT=1443;DATABASE={};UID={};PWD={}'.format
                          (driver, server, database, username, password))
    cursor = cnxn.cursor()
    return cursor
