package br.com.narcielitonlopes.cadastropessoas.service;

import java.util.ArrayList;
import java.util.List;

import br.com.narcielitonlopes.cadastropessoas.bean.PessoaBean;
import br.com.narcielitonlopes.cadastropessoas.model.Pessoa;

public class PessoaService {

    public void cadastrarPessoa(PessoaBean pessoaBean){
        Pessoa pessoa = new Pessoa(pessoaBean.getIdade(), pessoaBean.getNome(),
                pessoaBean.getSexo());
        pessoa.save();
    }

    public List<PessoaBean> listarPessoas(){

        List<Pessoa> pessoas = Pessoa.listAll(Pessoa.class);

        List<PessoaBean> pessoaBeanList = new ArrayList<>();

        for (PessoaBean  bean : pessoaBeanList ) {
            PessoaBean pessoaBean = new PessoaBean();
            pessoaBean.setId(bean.getId());
            pessoaBean.setNome(bean.getNome());
            pessoaBean.setSexo(bean.getSexo());
            pessoaBean.setIdade(bean.getIdade());

            pessoaBeanList.add(pessoaBean);
        }

        return pessoaBeanList;
    }

    public Long countSexo(String sexo){
        String[] vals = {
                String.valueOf(sexo)
        };
        return Pessoa.count(Pessoa.class, "sexo = ?", vals);
    }

}
