from enum import Enum
from Server.Objects import ApiMethod
from Server.Objects import Object
from Server.Objects import Status
import collections


class ParamRequests(Enum):
    Params = "params"
    Min = "min_params"
    Count = "count"
    TypeRequest = "type_req"

# params - допустимые параметры в запросе
# min_params - минимальное количество параметров, допустимое для запросов
# count - количество параметров в запросе
# type - тип запроса, 1 - get, 0 - post


paramsRequests = {
    ApiMethod.GetTowns.value: {'params': {'token': 'token'}, 'min_params': 1, 'count': 1, 'type_req': 1},

    ApiMethod.GetWitcherDesiredContract.value: {'params': {'token': 'token', 'id': 'id'}, 'min_params': 2, 'count': 2,
                                                'type_req': 1},

    ApiMethod.GetListComments.value: {'params': {'token': 'token', 'type': {'profile': 'profile',
                                                                            'contract': 'contract'},
                                      'id': 'id'}, 'min_params': 3, 'count': 3, 'type_req': 1},

    ApiMethod.RefuseContract.value: {'params': {'token': 'token', 'id_contract': 'id_contract'}, 'min_params': 2,
                                     'count': 2, 'type_req': 0},

    ApiMethod.AnswerWitcherInContract.value: {'params': {'token': 'token', 'status': 'status',
                                                         'id_contract': 'id_contract'},
                                              'min_params': 3, 'count': 3, 'type_req': 0},

    ApiMethod.SelectWitcherInContract.value: {'params': {'token': 'token', 'id_witcher': 'id_witcher',
                                                         'id_contract': 'id_contract'},
                                              'min_params': 3, 'count': 3, 'type_req': 0},

    ApiMethod.AddCommentContract.value: {'params': {'token': 'token', 'text': 'text', 'id': 'id'},
                                         'min_params': 3, 'count': 3, 'type_req': 0},

    ApiMethod.AddWitcherInContract.value: {'params': {'token': 'token', 'id': 'id'},
                                           'min_params': 2, 'count': 2, 'type_req': 0},

    ApiMethod.GetAdvert.value: {'params': {'token': 'token', 'id': 'id'}, 'min_params': 2, 'count': 2, 'type_req': 1},

    ApiMethod.GetProfile.value: {'params': {'token': 'token', 'id': 'id'}, 'min_params': 2, 'count': 2, 'type_req': 1},

    ApiMethod.DeleteAdvert.value: {'params': {'token': 'token', 'id': 'id'}, 'min_params': 2, 'count': 2, 'type_req': 1},

    ApiMethod.EditAdvert.value: {'params': {'token': 'token', 'id': 'id', 'id_witcher': 'id_witcher',
                                            'status': 'status', 'id_task_located': 'id_task_located', 'text': 'text',
                                            'bounty': 'bounty', 'photo_del': 'photo_del', 'photo_new': 'photo_new',
                                            'header': 'header'},
                                 'min_params': 3, 'count': 10, 'type_req': 0},

    ApiMethod.CreateAdvert.value: {'params': {'token': 'token', 'id_task_located': 'id_task_located',
                                              'text': 'text', 'bounty': 'bounty', 'photo': 'photo', 'header': 'header'},
                                   'min_params': 4, 'count': 6, 'type_req': 0},

    ApiMethod.AddCommentProfile.value: {'params': {'token': 'token', 'text': 'text', 'id': 'id'},
                                        'min_params': 3, 'count': 3, 'type_req': 0},

    ApiMethod.EditProfile.value: {'params': {'token': 'token', 'about': 'about', 'name': 'name',
                                             'photo': 'photo', 'password': 'password'},
                                  'min_params': 2, 'count': 5, 'type_req': 0},

    ApiMethod.GetListContracts.value: {'params': {'token': 'token',
                                                  'filter': {'locate': {'town': 'town', 'kingdom': 'kingdom'},
                                                             'bounty': {'min': 'min', 'max': 'max'}},
                                                  'sort': {'alph': 'alph', 'locate': 'locate',
                                                           'lastupdate': 'lastupdate'},
                                                  'sortype': {'asc': 'asc', 'desc': 'desc'}},
                                       'min_params': 1, 'count': 6, 'type_req': 1},

    ApiMethod.Registration.value: {'params': {'login': 'login', 'password': 'password', 'isWitcher': 'isWitcher'},
                                   'min_params': 3, 'count': 3, 'type_req': 0},

    ApiMethod.Authorization.value: {'params': {'login': 'login', 'password': 'password', 'fcm_token': 'fcm_token'},
                                    'min_params': 3, 'count': 3, 'type_req': 0},

    ApiMethod.GetContractClient.value: {'params': {'token': 'token',
                                                  'filter': {'locate': {'town': 'town', 'kingdom': 'kingdom'},
                                                             'bounty': {'min': 'min', 'max': 'max'}},
                                                  'sort': {'alph': 'alph', 'locate': 'locate',
                                                           'lastupdate': 'lastupdate'},
                                                  'sortype': {'asc': 'asc', 'desc': 'desc'}},
                                       'min_params': 1, 'count': 6, 'type_req': 1},

    ApiMethod.GetContractWitcher.value: {'params': {'token': 'token',
                                                  'filter': {'locate': {'town': 'town', 'kingdom': 'kingdom'},
                                                             'bounty': {'min': 'min', 'max': 'max'}},
                                                  'sort': {'alph': 'alph', 'locate': 'locate',
                                                           'lastupdate': 'lastupdate'},
                                                  'sortype': {'asc': 'asc', 'desc': 'desc'}},
                                       'min_params': 1, 'count': 6, 'type_req': 1},

    ApiMethod.ContractComplited.value: {'params': {'token': 'token', 'id': 'id'}, 'min_params': 2, 'count': 2,
                                        'type_req': 1}}


def security_requests(method, params, isget=1):
    is_ok, status = False, Object()
    status.status = Status.Error.value

    if paramsRequests.get(method, None) is not None:
        if isget == paramsRequests[method][ParamRequests.TypeRequest.value]:
            count, minimum, par_req = paramsRequests[method][ParamRequests.Count.value], \
                                      paramsRequests[method][ParamRequests.Min.value], \
                                      dict(paramsRequests[method][ParamRequests.Params.value])
            if len(params) - isget <= count:
                current_params = 0
                bad_params = False
                params_order = sort_dict(params, paramsRequests[method][ParamRequests.Params.value].keys())

                def is_dict(d): return type(d) is dict
                try:
                    for i in params_order.keys():
                        if i != 'method' and is_dict(par_req[i]) and is_dict(par_req[i][params_order[i]]):
                            par_req.update(par_req.pop(i)[params_order[i]])
                        current_params += 1

                except KeyError:
                    bad_params = True

                if bad_params:
                    status.message = "UnknownParams"
                elif current_params < minimum:
                    status.message = "FewParams"
                else:
                    status.status = Status.Ok.value
                    is_ok = True
            else:
                status.message = "ManyParams"
        else:
            status.message = "InvalidRequestType"
    else:
        status.message = "UnknownRequests"

    status = status.toJSON()
    return is_ok, status


def sort_dict(dict_, order_keys):
    res = collections.OrderedDict()

    for key in order_keys:
        if dict_.get(key) is not None:
            res[key] = dict_[key]

    for key in dict_.keys():
        res[key] = dict_[key]

    return res
