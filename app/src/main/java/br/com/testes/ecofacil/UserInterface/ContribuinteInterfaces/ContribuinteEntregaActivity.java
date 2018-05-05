package br.com.testes.ecofacil.UserInterface.ContribuinteInterfaces;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import br.com.testes.ecofacil.R;

public class ContribuinteEntregaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doador_entrega);

    }

    public void clickButtonIniciarEntrega(View view) {

        DialogFragment alertAdressDialogFragment = new AlertAdressDialogFragment();
        alertAdressDialogFragment.show(getFragmentManager(), "alertAdressDialogFragment");
    }

    @SuppressLint("ValidFragment")
    public class AlertAdressDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Ecoponto proximo da sua localização:")
                    .setMessage("Endereco: " + "variavelEndereco" + "\n" +
                                "Cidade: " + "variavelCidade" + "\n" +
                                "Estado: " + "variavelEstado" + "\n")
                    .setPositiveButton("Aceito", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            //Resposta positiva;
                            Toast.makeText(getApplication(), "A resposta selecionado foi 'Aceito'", Toast.LENGTH_SHORT).show();

                            //Redirecionamos o app para o google maps ou waze;
                            //if (googglemaps) {

                            // Create a Uri from an intent string. Use the result to create an Intent.
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=Estação+Calmon+Viana+-+Centro,+São+Paulo+-+SP");

                            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                            // Make the Intent explic   it by setting the Google Maps package
                            mapIntent.setPackage("com.google.android.apps.maps");

                            //Verificamos se o maps está disponível para uso
                            if (mapIntent.resolveActivity(getPackageManager()) != null) {

                                // Attempt to start an activity that can handle the Intent
                                startActivity(mapIntent);
                            }
                            else {

                                Toast.makeText(getApplicationContext(), "Google Maps indisponível ou fora de atualização", Toast.LENGTH_SHORT);
                            }
                            //} else if (waze) {


                            //}
                        }
                    })
                    .setNegativeButton("Procurar outro ponto", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            //Respostas negativa
                            Toast.makeText(getApplication(), "A resposta selecionado foi 'Procurar outro ponto'", Toast.LENGTH_SHORT).show();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        @Override
        public int show(FragmentTransaction transaction, String tag) {
            return super.show(transaction, tag);
        }
    }
}
