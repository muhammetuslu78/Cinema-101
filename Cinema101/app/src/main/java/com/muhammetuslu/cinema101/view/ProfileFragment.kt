package com.muhammetuslu.cinema101.view


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.muhammetuslu.cinema101.view.main.LoginActivity

import com.muhammetuslu.cinema101.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*


class ProfileFragment : Fragment() {
    var selectedPP:Uri?=null

    lateinit var logOutButton:Button
    lateinit var fab :FloatingActionButton
    lateinit var save :TextView
    lateinit var mail :TextView
    lateinit var id:TextView
    lateinit var name:TextView

    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =inflater.inflate(R.layout.fragment_profile, container, false)

        auth= FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        logOutButton=view.findViewById(R.id.btn_log_out)
        fab = view.findViewById(R.id.fab_camera)
        save = view.findViewById(R.id.btnSave)
        mail = view.findViewById(R.id.profileMail)
        id = view.findViewById(R.id.tv_Id)
        name=view.findViewById(R.id.profileFullName)

        mail.text = auth.currentUser!!.email
        id.text=auth.currentUser!!.uid


        fab.setOnClickListener {
           if (ContextCompat.checkSelfPermission(context!!,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
           {
               ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
           }
            else
           {
               val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
               startActivityForResult(intent,2)
           }
        }

        logOutButton.setOnClickListener {
            val intent=Intent(activity, LoginActivity::class.java)
            activity?.startActivity(intent)
            auth.signOut()
            activity?.finish()

        }
        getDataFromFirestore()

        save.setOnClickListener {
            val uuid = UUID.randomUUID()
            val imageName="$uuid.jpg"

            val storage = FirebaseStorage.getInstance()
            val reference = storage.reference
            val imagesReference=reference.child("images").child(imageName)

            if (selectedPP != null)
            {
                imagesReference.putFile(selectedPP!!).addOnSuccessListener {taskSnapshot ->

                    val uploadedPictureReference=FirebaseStorage.getInstance().reference.child("images").child(imageName)
                    uploadedPictureReference.downloadUrl.addOnCompleteListener {uri ->
                        val downloadUrl = uri.toString()
                        println(downloadUrl)

                        val postMap = hashMapOf<String,Any>()
                        postMap.put("downloadUrl",downloadUrl)
                        postMap.put("userEmail",auth.currentUser!!.email.toString())
                        postMap.put("username",profileFullName.text.toString())

                        db.collection("Posts").add(postMap).addOnCompleteListener { task ->
                            if (task.isComplete && task.isSuccessful)
                            {
                            }
                        }.addOnFailureListener { exception ->
                            Toast.makeText(context,exception.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==1) {
            if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==2 && resultCode==Activity.RESULT_OK && data != null)
        {
            selectedPP=data.data
            try {
                if (selectedPP != null)
                {
                    if (Build.VERSION.SDK_INT >= 28)
                    {
                        val source = ImageDecoder.createSource(activity!!.contentResolver,selectedPP!!)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        uploadImageView.setImageBitmap(bitmap)
                    }
                    else
                    {
                        val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver,selectedPP)
                        uploadImageView.setImageBitmap(bitmap)
                    }
                }
            }
            catch (e:Exception) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun getDataFromFirestore()
    {
        db.collection("Posts").addSnapshotListener { snapshot, exception ->
            if(exception != null)
            {
                Toast.makeText(context,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
            else
            {
                if(snapshot != null)
                {
                    if(!snapshot.isEmpty)
                    {
                        val documents = snapshot.documents

                        for (i in documents)
                        {
                            val useremail = i.get("userEmail") as String


                            if (useremail.equals(auth.currentUser!!.email.toString()))
                            {
                                val downloadUrl = i.get("downloadUrl") as String
                                val username = i.get("username") as String

                                if (username != null && downloadUrl != null)
                                {
                                profileFullName.setText(username)
                                Picasso.get().load(downloadUrl).resize(120,120).into(uploadImageView)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
