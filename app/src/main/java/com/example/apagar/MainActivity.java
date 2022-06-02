package com.example.apagar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayList<Pessoa> pessoaList = new ArrayList<>();

    ListView listView;
    ArrayAdapter<Pessoa> pessoaArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listView  = findViewById(R.id.listView1);

        iniciarFirebase();
        
        Pessoa pessoa = new Pessoa();
        pessoa.setEmail("IFMS2@ifms.edu.br");
        pessoa.setNome("IFMS2");
        pessoa.setId(11);

        Pessoa pessoa1 = new Pessoa();
        pessoa1.setEmail("IFMS2@ifms.edu.br");
        pessoa1.setNome("IFMS2ed");
        pessoa1.setId(11);

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setEmail("IFMS2@ifms.edu.br");
        pessoa2.setNome("IFMS2e");
        pessoa2.setId(11);

        Pessoa pessoa3 = new Pessoa();
        pessoa3.setEmail("IFMS2@ifms.edu.br");
        pessoa3.setNome("IFMS2asd");
        pessoa3.setId(11);

        //Inserir
        databaseReference.child("Usuário").
                child(pessoa.getNome()).
                setValue(pessoa);

        databaseReference.child("Usuário").
                child(pessoa2.getNome()).
                setValue(pessoa2);

        databaseReference.child("Usuário").
                child(pessoa3.getNome()).
                setValue(pessoa3);

//        Listar:
        String palavra = "";
        pesquisarPalavra(palavra);

        //remover
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Pessoa p1 = pessoaList.get(i);

                //se não limpar o array, não renderiza a lista corretamente
                //ouvinte addValueEventListener repopula ele com add.
                pessoaList.clear();

                databaseReference.child("Usuário").child(p1.getNome()).removeValue();

            }
        });

    }
    private void pesquisarPalavra(String palavra) {
        Query query;

        if (palavra.equals("")) {
            query = databaseReference.child("Usuário").orderByChild("nome");
        }
        else{
            query = databaseReference.child("Usuário").orderByChild("nome").
                    startAt(palavra).endAt(palavra + "\uf8ff");
        }
        pessoaList.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objDataSnapshot1: dataSnapshot.getChildren()) {
                    Pessoa p = objDataSnapshot1.getValue(Pessoa.class);
                    pessoaList.add(p);
                }

                pessoaArrayAdapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1, pessoaList);

                //não esquecer do toString na model pois o arraylist é de objetos.
                //não esquecer de definir altura para a lista no xml.
                listView.setAdapter(pessoaArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void iniciarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }
}