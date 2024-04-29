package com.example.journalchat.ui.validatiors

import com.example.journalchat.ui.validatiors.Constants.MAX_NAME_LENGTH
import com.example.journalchat.ui.validatiors.Constants.MIN_NAME_LENGTH

class ChatCreationValidator{
    fun validateName(text: String): ValidationResult{
        if (text.isBlank()){
            return ValidationResult(false,"Name cannot be blank.")
        }else if(text.length > MAX_NAME_LENGTH){
            return ValidationResult(false,"Name cannot be longer than $MAX_NAME_LENGTH characters.")
        }else if(text.length > MIN_NAME_LENGTH){
            return ValidationResult(false,"Name cannot be shorter than $MIN_NAME_LENGTH characters.")
        }
        return ValidationResult(true, null)
    }


 }

