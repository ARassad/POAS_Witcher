package ServerExchange.ServerRequests;

import java.util.HashMap;

import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;


/**
 * Created by Дима on 18.02.2018.
 */

public class AddCommentProfileRequest extends TokenServerRequest<Boolean> {

    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }
	
	private String ADD_COMMENT_PROFILE_METHOD_NAME = "AddCommentProfile";

    private String text;
    private long profile_id;

    public AddCommentProfileRequest(String address) {super(address);}
    public AddCommentProfileRequest() {super();}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("text", text);
        params.put("id", profile_id);

        return new ServerMethod(ADD_COMMENT_PROFILE_METHOD_NAME, params);
    }

    class JsonAddCommentProfileServerAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {


        return JsonAddCommentProfileServerAnswer.class;
    }

    public void addCommentProfile(String text, long id, IServerAnswerHandler onAddCommentProfileHandler) {
        this.text = text;
        this.profile_id = id;
        startRequest(onAddCommentProfileHandler);
    }

    public void getLoggedProfile(String text, IServerAnswerHandler onGetProfileHandler){
        addCommentProfile(text, LoginRequest.getLoggedUserId(), onGetProfileHandler);
    }

}
