package ServerExchange.ServerRequests.ServerAnswerHandlers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import java.io.EOFException;
import java.lang.ref.WeakReference;

/**
 * Created by Dryush on 22.02.2018.
 */

public abstract class DefaultServerAnswerHandler<AnswerType> implements IServerAnswerHandler<AnswerType> {

    protected Context context = null;
    protected AlertDialog.Builder dlgAlert  = null;
    protected WeakReference<Activity> weakActivity;

    public DefaultServerAnswerHandler(Activity context){
        this.context = context;
        weakActivity = new WeakReference<Activity>(context);
        dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setTitle("Упс, у нас ошибочка");
        dlgAlert.setCancelable(true);
    }

    public abstract void handle(AnswerType answ) throws Exception;

    public void errorHandle(String errorMessage){

        dlgAlert.setMessage(errorMessage);
        dlgAlert.create().show();

    }

    public void exceptionHandle(Exception excp){
        dlgAlert.setMessage(excp.getMessage());
        dlgAlert.create().show();
    }

    @Override
    public void fullAnswerHandle(AnswerType answ) {
        if (weakActivity != null && !weakActivity.get().isFinishing()){
            try{
                handle(answ);
            } catch (Exception e){
                dlgAlert.setMessage("Ошибка при обработке запроса в " +
                        this.getClass().getName() +"\n" +
                        e.getMessage());
                dlgAlert.create().show();
            }
        }
    }
}
