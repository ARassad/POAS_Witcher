/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iwitcherclientpart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author dryush
 */
abstract public class CommentsContainer implements ICommented{
    LinkedList<Comment> comments;

    public ArrayList<Comment> getComments() {
        return new ArrayList<Comment>(comments);
    }
    
    public ArrayList<Comment> addComment(Comment com){
        comments.addLast(com);
        return getComments();
    }

    public CommentsContainer() {
        comments = new LinkedList<>();
    }
    
    public CommentsContainer(Comment[] comments){
        this.comments = new LinkedList<>(Arrays.asList(comments));
    }

   
}
