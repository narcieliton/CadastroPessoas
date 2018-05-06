package br.com.narcielitonlopes.cadastropessoas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.orm.SugarContext;

import br.com.narcielitonlopes.cadastropessoas.bean.PessoaBean;
import br.com.narcielitonlopes.cadastropessoas.service.PessoaService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String sexo;

    @BindView(R.id.spinner_sexo) Spinner spinnerSexo;
    @BindView(R.id.edit_nome) EditText editTextNome;
    @BindView(R.id.edit_idade) EditText editTextIdade;
    @BindView(R.id.button_salvar) Button buttonSalvar;
    @BindView(R.id.button_gerar_relatorio) Button buttonGerarRelatorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SugarContext.init( this );

        buttonSalvar.setOnClickListener(this);
        buttonGerarRelatorio.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.sexo, R.layout.support_simple_spinner_dropdown_item);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSexo.setAdapter(adapterSpinner);

        spinnerSexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    sexo = "M";
                }else{
                    sexo = "F";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == buttonSalvar.getId()){
            if(!TextUtils.isEmpty(editTextNome.getText())
                    && !TextUtils.isEmpty(editTextIdade.getText())
                    && !TextUtils.isEmpty(sexo)){

                PessoaBean pessoaBean = new PessoaBean();
                pessoaBean.setNome(editTextNome.getText().toString());
                pessoaBean.setIdade(Integer.valueOf(editTextIdade.getText().toString()));
                pessoaBean.setSexo(sexo);

                PessoaService pessoaService =  new PessoaService();
                pessoaService.cadastrarPessoa(pessoaBean);
                this.cleanValues();
            }
        }
        else if(id == buttonGerarRelatorio.getId()){
            Intent intent = new Intent(this, RelatorioGraficoActivity.class);
            startActivity(intent);
        }
    }

    private void cleanValues(){
        editTextIdade.setText(null);
        editTextNome.setText("");
        spinnerSexo.setSelected(false);
    }

}
