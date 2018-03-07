package ServerExchange.ServerRequests.ServerAnswerHandlers;

/**
 * Created by Dryush on 14.02.2018.
 *
 * Интерфейс класса обработчика ответов запроса.
 * AnswerType - тип, ожидаемого ответа
 */

public interface IServerAnswerHandler<AnswerType>{

    /**
     * Данный метод вызывается, когда ответ от сервера получен.
     * В нём вы можете обработать как-либо ответ. Например, вывести во вьюшку
     *
     *
     * @param answ - ответ, который придёт с сервера
     *
     */
    void handle(AnswerType answ) throws Exception;


    /**
     * Данный метод вызывается перед методом handle(), в случае, если сервер вернул сообщение об ошибке
     * В таком случае ответ, скорее в handle() придёт NULL или False
     * @param errorMessage
     */
    void errorHandle(String errorMessage);

    /**
     * В данный методо передаётся Exception, если вдруг такой возник
     * Это может быть, как неудачная попытка соединения с серверов,
     * Так и что либо ещё, например, наш косяк в коде, попытка обратиться к null  и т.п.
     * @param excp
     */
    void exceptionHandle(Exception excp);

    /**
     * Теперь, именно этот метод будет вызываться при обработке запроса
     * Использовать для обёртки handle для проверок ответа и возможности его записать
     *
     * @param answ
     */
    void fullAnswerHandle(AnswerType answ);
}