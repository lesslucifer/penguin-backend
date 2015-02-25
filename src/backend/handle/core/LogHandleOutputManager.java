/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.handle.core;

/**
 *
 * @author Salm
 */
public interface LogHandleOutputManager {
    void accept(LogHandleOutput output);
    void renderOutput(StringBuilder sb);
}
