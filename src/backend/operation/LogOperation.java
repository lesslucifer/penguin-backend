/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.operation;

import backend.util.SwingEventHost;

/**
 *
 * @author Salm
 */
public interface LogOperation<T, P> extends Runnable, SwingEventHost<T, P> {
    int getTotalTasks();
    void stop();
}