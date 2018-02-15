package ServerExchange.ServerRequests;

/**
 * Created by Dryush on 14.02.2018.
 */

public interface IServerAnswerHandler<AnswerType>{
    void handle(AnswerType answ);
}