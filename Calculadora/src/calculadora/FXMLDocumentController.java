package calculadora;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import pt.ubi.ihc.Registadora;

/**
 * FXML Controller class
 *
 * @author jpc
 */
    public class FXMLDocumentController implements Initializable {
    @FXML
    private Label output;

    @FXML
    private TextArea historico;


    private StringBuilder valor;
    private Registadora registadora;
    private boolean float_point;
    private boolean is_result_pressed;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reset_calculator();
    }

    private void reset_calculator() {
        registadora = new Registadora();
        valor = new StringBuilder();

        float_point = false;
        is_result_pressed = false;

        output.setText("0.000");
        historico.setText("");
    }


    @FXML
    private void handle_button_action(ActionEvent event) {
        Button button = (Button) event.getSource();
        char identifier = button.getText().charAt(0);

        if (Character.isDigit(identifier)) // Digito
        {
            handleNumero(identifier);
        } else if (identifier == '.') // Floating point
        {
            handle_floating_point();
        } else if ("+-x/".indexOf(identifier) != -1) // Operador
        {
            handleOperador(identifier);
        } else if (identifier == 'C') // Limpar
        {
            process_clear();
        } else if (identifier == 'R') // Resultado
        {
            if (valor.length() > 0 && !is_result_pressed) {
                registadora.regista(Double.parseDouble(valor.toString()));

                is_result_pressed = true;

                // Update the display
                historico.appendText("\n---------------\n" + registadora.getResultado());
                output.setText(String.valueOf(registadora.getResultado()));

                // Clear the number
                valor.setLength(0);
            }
        }
    }

    @FXML
    protected void handleNumero(char digito) {
        if (!is_result_pressed) {
            valor.append(digito);
            historico.appendText(String.valueOf(digito));
        }
    }

    @FXML
    private void handle_floating_point() {
        if (!float_point && !is_result_pressed) {
            valor.append('.');
            historico.appendText(".");
            float_point = true;
        }
    }

    @FXML
    protected void handleOperador(char operator) {
        if (valor.length() == 0 && operator == '-' && !is_result_pressed) {
            valor.append(operator);
            historico.appendText("-");
        } else if (valor.length() > 0 && valor.charAt(0) != '-') {
            registadora.regista(Double.parseDouble(valor.toString()));
            registadora.defineOperador(operator);

            // Update the display
            historico.appendText(operator + "\n");
            output.setText(String.valueOf(registadora.getResultado()));

            is_result_pressed = false;

            // Clear the number
            valor.setLength(0);
        } else if (is_result_pressed) {
            registadora.defineOperador(operator);

            // Update the display
            historico.appendText(operator + "\n");
            output.setText(String.valueOf(registadora.getResultado()));

            is_result_pressed = false;
        }
    }

    @FXML
    private void process_clear() {
        reset_calculator();
    }
}
