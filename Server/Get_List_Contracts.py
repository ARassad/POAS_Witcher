from Server.Objects import Object, Status
from enum import Enum
from Server.Objects import User


class EventGetListContracts(Enum):
    SuccessGetListContracts = "Success"
    IncorrectParams = "IncorrectParams : "
    MinParamMiss = "ParamMinMissing"
    MaxParamMiss = "ParamMaxMissing"
    TownParamMiss = "townParamMissing"
    StatusParamMiss = "statusParamMissing"
    FilterLocateErr = "MissingTownandKingdomParams"
    SortErr = "ParamsSortError"


class Params:

    class Filter:
        Name = "filter"
        Bounty = "bounty"
        Locate = "locate"

    class Sort:
        Name = "sort"
        Alph = "alph"
        Locate = "locate"
        LastUpdate = "lastupdate"

    class SortType:
        Name = "sortype"
        Asc = "asc"
        Desc = "desc"

    Min = "min"
    Max = "max"
    Town = "town"
    Kingdom = "kingdom"
    Status = "status"


def get_list_contracts(cursor, params):
    return list_contract(cursor, params)


def get_contract_client(cursor, params):
    cursor.execute("select * from Client where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()

    return list_contract(cursor, params, id_client=row[0])


def get_contract_witcher(cursor, params):
    cursor.execute("select * from Witcher where id_profile=(select id_profile from Token_Table where token='{}')"
                   .format(params[User.Token.value]))
    row = cursor.fetchone()

    return list_contract(cursor, params, id_witcher=row[0])


def list_contract(cursor, params, **kwargs):
    req = 'select * from Contract'

    obj = Object()
    status = Object()
    status.status = Status.Ok.value
    obj.message = EventGetListContracts.SuccessGetListContracts.value

    if kwargs.get('id_witcher') is not None:
        req += ' inner join Desired_Contract on Desired_Contract.id_contract = Contract.id \
                 where Desired_Contract.id_witcher={}'.format(kwargs.get('id_witcher'))
    elif kwargs.get('id_client') is not None:
        req += ' where id_client={}'

    filtr = params.get(Params.Filter.Name)
    if filtr is not None:
        req += " and "
        if filtr == Params.Filter.Bounty:
            min = params.get(Params.Min)
            max = params.get(Params.Max)

            if min is None or max is None:
                status.status = Status.Error.value
                obj.value = "ERROR:{}{}".format(EventGetListContracts.MinParamMiss if min is None else "",
                                                EventGetListContracts.MaxParamMiss if max is None else "")
            else:
                req += " Bounty > {} and Bounty < {}".format(min, max)

        elif filtr == Params.Filter.Locate:
            town = params.get(Params.Town)
            kingdom = params.get(Params.Kingdom)

            if town is None and kingdom is None:
                status.status = Status.Error.value
                obj.value = EventGetListContracts.FilterLocateErr.value
            else:
                req += " id_task_located in (select id from Town where"

                if town is not None:
                    req += " Town.name = '{}'".format(town)

                    if kingdom is not None:
                        req += " and"
                    else:
                        req += ')'

                if kingdom is not None:
                    req += " Town.id_kingdom in (select id from Kingdom where Kingdom.name = '{}'))".format(kingdom)

    stat = params.get(Params.Status)
    if stat is not None and (kwargs.get('id_witcher') is not None or kwargs.get('id_client') is not None):
        req += " and status={}".format(stat)

    sort = params.get(Params.Sort.Name)
    if sort is not None:
        req += " order by"
        if sort == Params.Sort.Alph:
            req += " header"
        elif sort == Params.Sort.Locate:
            req += " id_task_located"
        elif sort == Params.Sort.LastUpdate:
            req += " last_update"
        else:
            status.status = Status.Error.value
            obj.value = EventGetListContracts.SortErr.value

        sort_type = params.get(Params.SortType.Name)
        if sort_type is not None and sort_type == Params.SortType.Desc:
            req += " desc"

    cursor.execute(req)
    row = cursor.fetchall()

    cursor.execute("select name from sys.columns where object_id = OBJECT_ID('dbo.Contract')")
    headers = cursor.fetchall()

    obj.contracts = {}
    for N, i in enumerate(row):
        line = Object()
        for n, head in enumerate(headers):
            setattr(line, head[0], i[n])

        if hasattr(line, 'id_task_located'):
            cursor.execute('select a.name, b.name from Town as a inner join Kingdom as b on a.id_kingdom = b.id \
                            where a.id={}'.format(line.id_task_located))
            towns = cursor.fetchone()
            line.town = towns[0]
            line.kingdom = towns[1]
        else:
            print("(GET_LIST_COMMENTS) У таблицы бд или выходного списка не найден атрибут id_task_located")

        obj.contracts[N] = line

    status.object = obj
    return status.toJSON()
