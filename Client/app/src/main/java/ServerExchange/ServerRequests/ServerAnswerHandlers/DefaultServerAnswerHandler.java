package ServerExchange.ServerRequests.ServerAnswerHandlers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import java.io.EOFException;
import java.lang.ref.WeakReference;

/**
 * Created by Dryush on 22.02.2018.
 */

public abstract class DefaultServerAnswerHandler<AnswerType> implements IServerAnswerHandler<AnswerType> {

    protected WeakReference<Activity> weakActivity;

    public DefaultServerAnswerHandler(Activity context){
        weakActivity = new WeakReference<Activity>(context);
    }

    protected AlertDialog getDialog(String message){
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(weakActivity.get());

        dlgAlert.setTitle("Упс, у нас ошибочка");
        dlgAlert.setCancelable(true);
        dlgAlert.setMessage(message);
        return dlgAlert.create();
    }

    public abstract void handle(AnswerType answ) throws Exception;

    public void errorHandle(String errorMessage){
        if (weakActivity != null && !weakActivity.get().isFinishing()){
            getDialog("Сервер ругается:\n" + errorMessage).show();
        }
    }

    public void exceptionHandle(Exception excp){
        if (weakActivity != null && !weakActivity.get().isFinishing()) {
            getDialog(excp.getMessage()).show();
        }
    }

    @Override
    public void fullAnswerHandle(AnswerType answ) {
        if (weakActivity != null && !weakActivity.get().isFinishing()){
            try{
                handle(answ);
            } catch (Exception e){
                getDialog("Ошибка при обработке запроса в " +
                            this.getClass().getName() +"\n" +
                            e.getMessage()).show();
            }
        }
    }
}
