package no.ecm.utils.messages

class ExceptionMessages{

    companion object {

        fun notFoundMessage (type: String, paramValue: String, value: String): String {
            return "Can't find ${type.capitalize()} with $paramValue: $value"
        }

        fun inputFilterInvalid() : String {
            return "You can only use one filter at time"
        }

        fun offsetAndLimitInvalid(): String {
            return "Offset must be grater than 0 and limit must be greater than/or equal 1"
        }

        fun toLargeOffset(offset: Int): String {
            return "Too large offset: $offset"
        }

        fun missingRequiredField(field: String): String {
            return "Missing required field: ${field.capitalize()}"
        }

        fun invalidIdParameter(): String {
            return "Invalid id parameter. This should be a numeric string"
        }
        
        fun tooLargeOffset(size: Int): String {
            return "Too large offset, size of result is $size"
        }
        
        fun invalidTimeFormat(): String {
            return "Bad expireAt format!, this follows following formatting rules: \"yyyy-MM-dd HH:mm:ss\""
        }
        
        fun invalidSeatFormat(): String {
            return "Bad seat format!, this follows following formatting rules: \"[A-Z][0-9]{1,2}\""
        }
        
        fun invalidJsonFormat(): String {
            return "Invalid JSON-format"
        }
        
        fun idInCreationDtoBody(type: String): String {
            return "You cannot create a ${type.capitalize()} with predefined id"
        }
        
        fun idInPatchDtoBody(): String {
            return "Updating the id is not allowed"
        }
        
        fun createEntity(type: String): String {
            return "Error while creating a ${type.capitalize()}"
        }
        
        fun deleteEntity(type: String): String {
            return "Error while deleting ${type.capitalize()}"
        }
    
        fun updateEntity(type: String): String {
            return "Error while updating ${type.capitalize()}"
        }

        fun invalidParameter(required: String, received: String): String {
            return "Invalid parameter, expected: ${required.capitalize()}, but received: ${received.capitalize()}"
        }

        fun unableToParse(value: String): String {
            return "Unable to parse object variable: ${value.capitalize()}"
        }

        fun illegalParameter(value: String) : String{
            return "You should not provide parameter: ${value.capitalize()} in this request!"
        }

        fun resourceAlreadyExists(type: String, paramValue: String, value: String): String {
            return "${type.capitalize()} with ${paramValue.capitalize()} equal to: '${value.capitalize()}' already exists."
        }
        
        fun notMachingIds(): String {
            return "The given id in DTO doesn't match the id in the database"
        }

        fun notMachingIds(type: String): String {
            return "The given ${type.capitalize()} in DTO doesn't match the $type in the database"
        }

        fun subIdNotMatchingParentId(subId: String, parentId: String) : String {
            return "foreign key '${subId.capitalize()}' not match primary key '${parentId.capitalize()}'"
        }



    }

}