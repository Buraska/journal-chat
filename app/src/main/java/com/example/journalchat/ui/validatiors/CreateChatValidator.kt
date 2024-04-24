package com.example.journalchat.ui.validatiors

class CreateChatValidator{
    fun validateName(text: String): ValidationResult{
        if (text.isBlank()){
            return ValidationResult(false,"Name cannot be blank.")
        }else if(text.length > 128){
            return ValidationResult(false,"Name cannot be longer than 128 characters.")
        }
        return ValidationResult(true, null)
    }


 }

