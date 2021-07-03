package com.s26.personlist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.s26.personlist.databinding.ActivityPersonListBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.Serializable
import kotlin.collections.ArrayList

class PersonListActivity : AppCompatActivity() {
    companion object{
        const val  OUTPUT_PERSON= "outputPerson"
        const val OUTPUT_UUID = "outputUUID"
        const val REQUEST_CODE_EDIT = 1
        const val REQUEST_CODE_ADD = 100
        private const val PREF_PERSON_KEY = "package.com.s26.personlist"
        private const val UUID_KEY = "uuid"
    }

    private lateinit var binding: ActivityPersonListBinding
    private lateinit var preferences: SharedPreferences
    //Moshiインスタンス化
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    //Moshiに対して、変換に利用するクラスを教える
    private val moshiType = Types.newParameterizedType(List::class.java,Person::class.java)
    private val jsonAdapter = moshi.adapter<List<Person>>(moshiType)

    private val onItemClickListener= object: MyAdapter.Callback{
        override fun onItemClick(person: Person) {
            //クリック時の処理実装

            val intent: Intent = Intent(this@PersonListActivity, EditPersonActivity::class.java).apply {
                putExtra(OUTPUT_PERSON, person)
                putExtra(OUTPUT_UUID,person.uuid)
            }
            startActivityForResult(intent, REQUEST_CODE_EDIT)
        }
    }

    private val addClickListener = View.OnClickListener {
        val intent = Intent(this,NewPersonActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_ADD)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(PREF_PERSON_KEY, Context.MODE_PRIVATE)

        val jsonText = preferences.getString(PREF_PERSON_KEY,null)
        val persons = if (jsonText.equals(null)){
            arrayListOf()
        }else {
            //JSONデータをインスタンスに復元(復元失敗: null)
            val list: List<Person> = jsonAdapter.fromJson(jsonText) ?: return
            list as? ArrayList<Person> ?: return
        }
        binding.personList.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = MyAdapter(this, persons, onItemClickListener)
        }
        binding.addButton.setOnClickListener(addClickListener)
        preferences = getSharedPreferences(PREF_PERSON_KEY, Context.MODE_PRIVATE) ?: return
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val myAdapter = binding.personList.adapter
        val personList: ArrayList<Person> =
                if (myAdapter is MyAdapter) myAdapter.persons else return

        val adapter = binding.personList.adapter as MyAdapter
        val person = adapter.persons

        if (requestCode== REQUEST_CODE_EDIT) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    val oldData: Serializable? = data.getSerializableExtra(EditPersonActivity.OLD_PERSON)
                    val newData: Serializable? = data.getSerializableExtra(EditPersonActivity.NEW_PERSON)
                    val uuid = data.getLongExtra(EditPersonActivity.UUID, Long.MAX_VALUE)
                    if (oldData != null) {
                        for (result in personList.withIndex()) {
                            if (result.value.uuid == uuid) {
                                personList.removeAt(result.index)
                                break
                            }
                        }
                    }else if(newData != null && newData is Person) {
                        for (result in personList.withIndex()) {
                            if (result.value.uuid == uuid) {
                                personList.removeAt(result.index)
                                break
                            }
                        }
                        initTextView(newData)
                    }else finish()
                    //personをJSONに変換する
                    val json = jsonAdapter.toJson(personList)
                    //JSONをSheredPreferencesに書き込む
                    with(preferences.edit()){
                        putString(PREF_PERSON_KEY,json)
                        putLong(UUID_KEY,uuid)
                        apply()
                    }
                    adapter.notifyDataSetChanged()
                }
            }

        }
        if (requestCode==REQUEST_CODE_ADD){
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    val addData: Serializable? = data.getSerializableExtra(NewPersonActivity.EXTRA_PERSON_ADD)
                    val uuid = data.getLongExtra(EditPersonActivity.UUID, Long.MAX_VALUE)
                    when((addData != null) && (addData is Person)){
                        true -> initTextView(addData)
                        else ->finish()
                    }
                    val json = jsonAdapter.toJson(personList)
                    with(preferences.edit()){
                        putString(PREF_PERSON_KEY,json)
                        putLong(UUID_KEY,uuid)
                        apply()
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
    private fun initTextView(data:Person){
        val adapter = binding.personList.adapter as MyAdapter
        val person = adapter.persons
        person.add(Person(data.name, data.age, data.isMarried))
    }

}