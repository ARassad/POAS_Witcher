/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerExchange;

import java.util.ArrayList;

/**
 *
 * @author dryush
 */
interface ICommented {
    public ArrayList<Comment> getComments();
    
    public ArrayList<Comment> addComment(Comment com);
    
}
