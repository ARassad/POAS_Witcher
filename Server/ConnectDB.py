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
    cnxn = pyodbc.connect('DRIVER='+driver+';PORT=1433;SERVER='+server+';PORT=1443;DATABASE='+database+';UID='+username+';PWD='+password)
    cursor = cnxn.cursor()
    return cursor
