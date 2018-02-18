package ServerExchange.ServerRequests;

import java.util.HashMap;

/**
 * Created by Dryush on 18.02.2018.
 */

public class EditProfileRequest extends TokenServerRequest<Boolean>{

    final String METHOD_NAME = "EditProfile";

    final String INFO_PARAM_NAME = "about";
    final String NAME_PARAM_NAME = "name";
    final String PHOTO_PARAM_NAME = "photo";

    HashMap<String, String> params = new HashMap<>();

    @Override
    protected ServerMethod getMethod() {
        return new ServerMethod(METHOD_NAME, params);
    }

    class EditProfileJsonAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {
        return EditProfileJsonAnswer.class;
    }
}
