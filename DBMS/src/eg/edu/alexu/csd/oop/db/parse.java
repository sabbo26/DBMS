package eg.edu.alexu.csd.oop.db;
import java.util.regex.Pattern;

public class parse {

    public int Check(String Exp) {
        if (Pattern.matches("^\\s*create \\s*database \\s*\\w+\\s*;?\\s*$", Exp)) {
            return 1;
        }

        else if (Pattern.matches("^\\s*drop \\s*database \\s*\\w+\\s*;?\\s*$", Exp)) {
            return 2;
        }

        else if (Pattern.matches(
                "^\\s*create\\s+table\\s+\\w+\\s*\\(\\s*(\\w+\\s+(int|varchar)\\s*,\\s*)*(\\w+\\s*(int|varchar))\\s*\\)( \\s*values\\s*\\(\\s*('[a-zA-Z0-9\\s]+'\\s*,\\s*|\\d+\\s*,\\s*)*('[a-zA-Z0-9\\s]+'|\\d+)\\s*\\))?\\s*;?\\s*$", Exp)) {
            return 3;
        }

        else if (Pattern.matches("^\\s*drop \\s*table \\s*\\w+\\s*;?\\s*$", Exp)) {
            return 4;
        }

        else if (Pattern.matches("^\\s*select \\s*(\\*|((\\w+\\s*,\\s*)*(\\w+))) \\s*from \\s*\\w+\\s*;?\\s*", Exp)) {
            return 5;
        }

        else if (Pattern.matches("^\\s*select \\s*(\\*|((\\w+\\s*,\\s*)*(\\w+))) \\s*from \\s*\\w+ \\s*where \\s*\\w+\\s*(=|>|<)\\s*('[a-zA-Z0-9\\s]+'|\\d+)\\s*;?\\s*$", Exp)) {
            return 6;
        }

        else if (Pattern.matches("^\\s*insert \\s*into \\s*\\w+\\s*(\\(\\s*(\\w+\\s*,\\s*)*(\\w+)\\s*\\))? \\s*values\\s*\\(\\s*('[a-zA-Z0-9\\s]+'\\s*,\\s*|\\d+\\s*,\\s*)*('[a-zA-Z0-9\\s]+'|\\d+)\\s*\\)\\s*;?\\s*$", Exp)) {
            return 7;
        }

        else if (Pattern.matches(
                "^\\s*update \\s*\\w+ \\s*set \\s*(\\w+\\s*=\\s*(('[a-zA-Z0-9\\s]+')|([0-9]+))\\s*,\\s*)*(\\w+\\s*=\\s*(('[a-zA-Z0-9\\s]+')|([0-9]+))) \\s*where \\s*\\w+\\s*(=|>|<)\\s*(('[a-zA-Z0-9\\s]+')|([0-9]+))\\s*;?\\s*$",
                Exp)) {
            return 8;
        }

        else if (Pattern.matches(
                "^\\s*update \\s*\\w+ \\s*set \\s*(\\w+\\s*=\\s*(('[a-zA-Z0-9\\s]+')|([0-9]+))\\s*,\\s*)*(\\w+\\s*=\\s*(('[a-zA-Z0-9\\s]+')|([0-9]+)))\\s*;?\\s*$",
                Exp)) {
            return 9;
        }

        else if (Pattern.matches("^\\s*delete \\s*from \\s*\\w+ \\s*where \\s*\\w+\\s*(=|>|<)\\s*('[a-zA-Z0-9\\s]+'|\\d+)\\s*;?\\s*$", Exp)) {
            return 10;
        }

        else if (Pattern.matches("^\\s*delete \\s*from \\s*\\w+\\s*;?\\s*$", Exp)) {
            return 11;
        } else
            return 0;

    }

}