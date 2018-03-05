package ServerExchange.ServerRequests.ServerAnswerHandlers;

import android.app.AlertDialog;
import android.content.Context;

import java.io.EOFException;

/**
 * Created by Dryush on 22.02.2018.
 */

public abstract class DefaultServerAnswerHandler<AnswerType> implements IServerAnswerHandler<AnswerType> {

    protected Context context = null;
    protected AlertDialog.Builder dlgAlert  = null;

    public DefaultServerAnswerHandler(Context context){
        this.context = context;
        dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setTitle("Упс, у нас ошибочка");
        dlgAlert.setCancelable(true);
    }

    abstract public void handle(AnswerType answ);

    public void errorHandle(String errorMessage){

        dlgAlert.setMessage(errorMessage);
        dlgAlert.create().show();

    }

    public void exceptionHandle(Exception excp){
        dlgAlert.setMessage(excp.getMessage());
        dlgAlert.create().show();
    }
}
