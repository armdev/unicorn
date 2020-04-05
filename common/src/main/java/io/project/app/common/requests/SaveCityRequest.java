/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.project.app.common.requests;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author armena
 */
@Data
public class SaveCityRequest implements Serializable{
    
    private String key;
    private String city;
    private int expireSeconds;
}
