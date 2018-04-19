package Datos;

/**
 * Created by ramir on 4/8/2018.
 */

public class DataParserJ {


    public static String parsear(String s){
        String resultado="";
        s= s.replace("\n", " ");
        s= s.replace("\t", " ");
        s= s.replace("%C3%81", "Á");
        s= s.replace("%C3%89", "É");
        s= s.replace("%C3%8D", "Í");
        s= s.replace("%C3%93", "Ó");
        s= s.replace("%C3%9A", "Ú");
        s= s.replace("%C3%A1", "á");
        s= s.replace("%C3%A9", "é");
        s= s.replace("%C3%AD", "í");
        s= s.replace("%C3%B3", "ó");
        s= s.replace("%C3%BA", "ú");
        for(int i=0; i<s.length(); i++){
            String k= s.charAt(i)+"";
            Boolean estado= false;
            switch(k){
                case ":": resultado+="D";
                    estado=true;
                    break;
                case "/": resultado+="S";
                    estado=true;
                    break;
                case ".": resultado+="P";
                    estado=true;
                    break;
                case "_": resultado+="R";
                    estado=true;
                    break;
                case "-": resultado+="G";
                    estado=true;
                    break;
                case ",": resultado+="C";
                    estado=true;
                    break;
                case "&": resultado+="A";
                    estado=true;
                    break;
                case "%": resultado+="V";
                    estado=true;
                    break;
                case "=": resultado+="E";
                    estado=true;
                    break;
                case "?": resultado+="I";
                    estado=true;
                    break;
                case "@": resultado+="K";
                    estado=true;
                    break;
                case "!": resultado+="U";
                    estado=true;
                    break;
                case "#": resultado+="W";
                    estado=true;
                    break;

                case "¡": resultado+="B";
                    estado=true;
                    break;
                case "¿": resultado+="F";
                    estado=true;
                    break;
                case "<": resultado+="H";
                    estado=true;
                    break;
                case ">": resultado+="J";
                    estado=true;
                    break;
                case "[": resultado+="L";
                    estado=true;
                    break;
                case "]": resultado+="M";
                    estado=true;
                    break;
                case "(": resultado+="N";
                    estado=true;
                    break;
                case ")": resultado+="O";
                    estado=true;
                    break;
                case "\n": resultado+="Q";
                    estado=true;
                    break;
                case "\"": resultado+="T";
                    estado=true;
                    break;
                case " ": resultado+="X";
                    estado=true;
                    break;
                case ";": resultado+="Y";
                    estado=true;
                    break;
                case "$": resultado+="Z";
                    estado=true;
                    break;
                case "á": resultado+="_A";
                    estado=true;
                    break;
                case "é": resultado+="_E";
                    estado=true;
                    break;
                case "í": resultado+="_I";
                    estado=true;
                    break;
                case "ó": resultado+="_O";
                    estado=true;
                    break;
                case "ú": resultado+="_U";
                    estado=true;
                    break;
                case "Á": resultado+="_B";
                    estado=true;
                    break;
                case "É": resultado+="_C";
                    estado=true;
                    break;
                case "Í": resultado+="_D";
                    estado=true;
                    break;
                case "Ó": resultado+="_F";
                    estado=true;
                    break;
                case "Ú": resultado+="_G";
                    estado=true;
                    break;
                case "*": resultado+="_H";
                    estado=true;
                    break;
                case "{": resultado+="_J";
                    estado=true;
                    break;
                case "}": resultado+="_K";
                    estado=true;
                    break;
                default : resultado+="";
                    break;
            }
            if(!estado){
                if(k.equals(k.toLowerCase())){
                    resultado+=k;
                }
                else{
                    resultado+="_"+k.toLowerCase();
                }
            }}
        return resultado;
    }

    public static String deparsear(String s){
        String resultado="";
        Boolean estado= true;
        for(int i=0; i<s.length(); i++){
            String k= s.charAt(i)+"";
            if(!estado){
                estado=true;
                if(k.toUpperCase().equals(k)){
                    switch (k){
                        case "A": resultado+="á";
                            break;
                        case "E": resultado+="é";
                            break;
                        case "I": resultado+="í";
                            break;
                        case "O": resultado+="ó";
                            break;
                        case "U": resultado+="ú";
                            break;
                        case "B": resultado+="Á";
                            break;
                        case "C": resultado+="É";
                            break;
                        case "D": resultado+="Í";
                            break;
                        case "F": resultado+="Ó";
                            break;
                        case "G": resultado+="Ú";
                            break;
                        case "H": resultado+="*";
                            break;
                        case "J": resultado+="{";
                            break;
                        case "K": resultado+="}";
                            break;
                    }
                }
                else
                resultado+=k.toUpperCase();
            }
            else{
                switch(k){
                    case "D": resultado+=":";
                        estado=true;
                        break;
                    case "S": resultado+="/";
                        estado=true;
                        break;
                    case "P": resultado+=".";
                        estado=true;
                        break;
                    case "R": resultado+="_";
                        estado=true;
                        break;
                    case "G": resultado+="-";
                        estado=true;
                        break;
                    case "C": resultado+=",";
                        estado=true;
                        break;
                    case "A": resultado+="&";
                        estado=true;
                        break;
                    case "V": resultado+="%";
                        estado=true;
                        break;
                    case "E": resultado+="=";
                        estado=true;
                        break;
                    case "I": resultado+="?";
                        estado=true;
                        break;
                    case "K": resultado+="@";
                        estado=true;
                        break;
                    case "U": resultado+="!";
                        estado=true;
                        break;
                    case "W": resultado+="#";
                        estado=true;
                        break;
                    case "B": resultado+="¡";
                        estado=true;
                        break;
                    case "F": resultado+="¿";
                        estado=true;
                        break;
                    case "H": resultado+="<";
                        estado=true;
                        break;
                    case "J": resultado+=">";
                        estado=true;
                        break;
                    case "L": resultado+="[";
                        estado=true;
                        break;
                    case "M": resultado+="]";
                        estado=true;
                        break;
                    case "N": resultado+="(";
                        estado=true;
                        break;
                    case "O": resultado+=")";
                        estado=true;
                        break;
                    case "Q": resultado+="\n";
                        estado=true;
                        break;
                    case "T": resultado+="\"";
                        estado=true;
                        break;
                    case "X": resultado+=" ";
                        estado=true;
                        break;
                    case "Y": resultado+=";";
                        estado=true;
                        break;
                    case "Z": resultado+="$";
                        estado=true;
                        break;
                    case "_": resultado+="";
                        estado=false;
                        break;
                    default : resultado+=k;
                        break;
                }}}
        return resultado;
    }

}
