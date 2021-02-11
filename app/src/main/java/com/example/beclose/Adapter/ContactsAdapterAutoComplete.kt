package com.example.beclose.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.beclose.R
import com.example.beclose.VVM.Observers.AutoCompleteCallBack
import com.example.beclose.model.DataField
import kotlin.collections.ArrayList


class ContactsAdapterAutoComplete : ArrayAdapter<DataField>,Filterable {
    lateinit var arrayList:ArrayList<DataField>
    lateinit var fieldNameTV:TextView
    lateinit var autoCompleteClickListener:AutoCompleteCallBack
    fun setAutoCompleteClickListenerclick(autoCompleteClickListener:AutoCompleteCallBack){
        this.autoCompleteClickListener=autoCompleteClickListener
    }
    private var filter= object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filterResult= FilterResults()
            var suggestions=ArrayList<DataField>()
            if (constraint==null||constraint.length==0){
                suggestions.addAll(arrayList)
            }else{
                var filterPattern=constraint.toString().toLowerCase().trim()
                for(item in arrayList){
                    if (item.fieldName!!.toLowerCase().contains(filterPattern)){
                        suggestions.add(item)
                    }
                }
            }
            filterResult.values=suggestions
            filterResult.count=suggestions.size
            return filterResult
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            addAll(results!!.values as List<DataField>)
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as DataField).fieldName.toString()
        }
    }

    constructor(context: Context, resource: Int, objects: ArrayList< DataField>) : super(context, 0, objects){
        arrayList= objects
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filterResult= FilterResults()
                var suggestions=ArrayList<DataField>()
                if (constraint==null||constraint.length==0){
                    suggestions.addAll(arrayList)
                }else{
                    var filterPattern=constraint.toString().toLowerCase().trim()
                    for(item in arrayList){
                        if (item.fieldName!!.toLowerCase().contains(filterPattern)){
                            suggestions.add(item)
                        }
                    }
                }
                filterResult.values=suggestions
                filterResult.count=suggestions.size
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                if (results != null) {
                    addAll(results.values as List<DataField>)
                }
                notifyDataSetChanged()
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as DataField).fieldName.toString()
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view=convertView
        if (view==null){
            view =LayoutInflater.from(context).inflate(R.layout.contacts_item,parent,false)
        }
        var connectionIV:ImageView=view!!.findViewById(R.id.contacts_item_IV)
        fieldNameTV=view!!.findViewById(R.id.filedName_TV)
        var fieldContentTV:TextView=view!!.findViewById(R.id.fileContent_TV)
        connectionIV.setImageResource(arrayList.get(position).icon!!)
        fieldContentTV.setText("")
        fieldNameTV.setText(arrayList.get(position).fieldName)
        fieldNameTV.setOnClickListener {
            if(autoCompleteClickListener!=null){
                arrayList.get(position).fieldName?.let { it1 -> autoCompleteClickListener.getFieldName(it1) }
            }
        }
        connectionIV.setOnClickListener {
            arrayList.get(position).fieldName?.let { it1 -> autoCompleteClickListener.getFieldName(it1) }
        }
        fieldContentTV.setOnClickListener {
            arrayList.get(position).fieldName?.let { it1 -> autoCompleteClickListener.getFieldName(it1) }
        }
        return view!!
    }

    override fun getItem(position: Int): DataField? {
        return arrayList.get(position)
    }

    override fun getCount(): Int {
        return arrayList.size
    }
}
