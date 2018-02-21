
import by.intervale.test.task.beans.AdvCalc;
import by.intervale.test.task.beans.CalcFacade;
import by.intervale.test.task.beans.FunctCalc;
import by.intervale.test.task.beans.SimplCalc;
/**
 * Runner class
 * @author Igor Susolkin
 *
 */

public final class Runner {
    private Runner() {
        //not called
      }
    private static final  String INFILE_NAME_SIMPLE_CALC = "src/input_1.txt";
    private static final String OUTFILE_NAME_SIMPLE_CALC = "src/output_1.txt";
    private static final String INFILE_NAME_ADV_CALC = "src/input_2.txt";
    private static final String OUTFILE_NAME_ADV_CALC = "src/output_2.txt";
    private static final String INFILE_NAME_FUNCT_CALC = "src/input_3.txt";
    private static final String OUTFILE_NAME_FUNCT_CALC = "src/output_3.txt";
    /**
     *
     * @param args - input args
     */
    public static void main(final String[] args) {
        CalcFacade calc = new CalcFacade();
        calc.exec(INFILE_NAME_SIMPLE_CALC, OUTFILE_NAME_SIMPLE_CALC, new SimplCalc());
        calc.exec(INFILE_NAME_ADV_CALC, OUTFILE_NAME_ADV_CALC, new AdvCalc());
        calc.exec(INFILE_NAME_FUNCT_CALC, OUTFILE_NAME_FUNCT_CALC, new FunctCalc());
    }
}
