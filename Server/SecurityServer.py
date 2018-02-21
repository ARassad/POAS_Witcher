from enum import Enum
from Server.Objects import ApiMethod
from Server.Objects import Object
from Server.Objects import Status


class ParamRequests(Enum):
    Params = "params"
    Min = "min_params"
    Count = "count"


paramsRequests = {
    ApiMethod.GetTowns.value: {'params': {'token': 'token'}, 'min_params': 1, 'count': 1},
    ApiMethod.GetWitcherDesiredContract.value: {'params': {'token': 'token', 'id': 'id'}, 'min_params': 2, 'count': 2},
    ApiMethod.GetListComments.value: {'params': {'token': 'token', 'type': {'profile': 'profile', 'contract': 'contract'},
                                      'id': 'id'}, 'min_params': 3, 'count': 3},
    ApiMethod.RefuseContract.value: {'params': {'token': 'token', 'id_contract': 'id_contract'}, 'min_params': 2,
                                     'count': 2},
    ApiMethod.AnswerWitcherInContract.value: {'params': {'token': 'token', 'status': 'status',
                                                         'id_contract': 'id_contract'},
                                              'min_params': 3, 'count': 3},
    ApiMethod.SelectWitcherInContract.value: {'params': {'token': 'token', 'id_witcher': 'id_witcher',
                                                         'id_contract': 'id_contract'},
                                              'min_params': 3, 'count': 3},
    ApiMethod.AddCommentContract.value: {'params': {'token': 'token', 'text': 'text', 'id': 'id'},
                                         'min_params': 3, 'count': 3},
    ApiMethod.AddWitcherInContract.value: {'params': {'token': 'token', 'id': 'id'},
                                           'min_params': 2, 'count': 2},
    ApiMethod.GetAdvert.value: {'params': {'token': 'token', 'id': 'id'}, 'min_params': 2, 'count': 2},
    ApiMethod.GetProfile.value: {'params': {'token': 'token', 'id': 'id'}, 'min_params': 2, 'count': 2},
    ApiMethod.DeleteAdvert.value: {'params': {'token': 'token', 'id': 'id'}, 'min_params': 2, 'count': 2},
    ApiMethod.EditAdvert.value: {'params': {'token': 'token', 'id': 'id', 'id_witcher': 'id_witcher', 'status': 'status',
                            'id_task_located': 'id_task_located', 'text': 'text', 'bounty': 'bounty',
                            'photo_del': 'photo_del', 'photo_new': 'photo_new'}, 'min_params': 3, 'count': 3},
    ApiMethod.CreateAdvert.value: {'params': {'token': 'token', 'id_task_located': 'id_task_located',
                              'text': 'text', 'bounty': 'bounty', 'photo': 'photo'}, 'min_params': 4, 'count': 5},
    ApiMethod.AddCommentProfile.value: {'params': {'token': 'token', 'text': 'text', 'id': 'id'},
                                        'min_params': 3, 'count': 3},
    ApiMethod.EditProfile.value: {'params': {'token': 'token', 'about': 'about', 'name': 'name',
                             'photo': 'photo', 'password': 'password'}, 'min_params': 2, 'count': 5},
    ApiMethod.GetListContracts.value: {'params': {'token': 'token',
                                                  'filter': {'locate': {'town': 'town', 'kingdom': 'kingdom'},
                                                             'bounty': {'min': 'min', 'max': 'max'}},
                                  'sort': {'alph': 'alph', 'locate': 'locate', 'lastupdate': 'lastupdate'},
                                  'sortype': {'asc': 'asc', 'desc': 'desc'}}, 'min_params': 1, 'count': 6},
    ApiMethod.Registration.value: {'params': {'login': 'login', 'password': 'password', 'witcher': 'witcher'},
                                   'min_params': 3, 'count': 3},
    ApiMethod.Authorization.value: {'params': {'login': 'login', 'password': 'password'}, 'min_params': 2, 'count': 2}}


def security_requests(method, params, isget=1):
    is_ok, status = False, Object()
    status.status = Status.Error.value

    if paramsRequests.get(method, None) is not None:
        count, minimum, par_req = paramsRequests[method][ParamRequests.Count.value], \
                                  paramsRequests[method][ParamRequests.Min.value], \
                                  paramsRequests[method][ParamRequests.Params.value]
        if len(params) - isget <= count:
            current_params = 0
            bad_params = False
            for i in params.keys():
                if par_req.get(i, None) is None:
                    if i != 'method':
                        bad_params = True
                else:
                    current_params += 1
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
        status.message = "UnknownRequests"

    status = status.toJSON()
    return is_ok, status