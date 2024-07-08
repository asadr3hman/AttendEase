package com.example.attendancemanage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendancemanage.model.Student
import com.example.attendancemanage.model.Subject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StudentViewModel() : ViewModel() {
    val TAG = "StudentViewModel"
    private var _student = MutableLiveData<Student>()
    val student: LiveData<Student> get() = _student

    private val _getSomeStudentList = MutableLiveData<List<Student>>()
    val getSomeStudentList: LiveData<List<Student>> get() = _getSomeStudentList


    private val _studentList = MutableLiveData<List<Student>>()
    val studentList: LiveData<List<Student>> get() = _studentList

    private val _studentSubjects = MutableLiveData<List<Subject>>()
    val studentSubjects: LiveData<List<Subject>> get() = _studentSubjects

    private val db = FirebaseFirestore.getInstance()


    init {
        viewModelScope.launch {
            getSomeStudents()
        }
    }

    suspend fun getStudentData(uid: String): Student? {
        return try {
            val studentRef = db.collection("students").document(uid)
            val documentSnapshot = studentRef.get().await()
            Log.e(TAG, documentSnapshot.toObject<Student>().toString())
            documentSnapshot.toObject<Student>()
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching student data", e)
            null
        }
    }

    fun addStudentWithSubjects(student: Student) {
        viewModelScope.launch {
            student.uid?.let {
                db.collection("students").document(it).set(student).addOnSuccessListener {
                    _student.postValue(student)
                    Log.v("StudentViewModel", "Student has been stored in the database")
                }.addOnFailureListener {
                    Log.v(
                        "StudentViewModel",
                        "Student has not been stored in the database. Something went wrong"
                    )
                }
            }
        }
    }

    fun getSubjectsForStudent(uid: String) {
        viewModelScope.launch {
            Log.v(TAG, uid)
            val subjects = getSubjectsForStudentFromFirestore(uid)
            if (subjects != null) {
                _studentSubjects.postValue(subjects!!)
            }
            Log.v(TAG, _studentSubjects.value.toString())
        }
    }

    private suspend fun getSubjectsForStudentFromFirestore(uid: String): List<Subject>? {
        val studentRef = db.collection("students").document(uid)
        val documentSnapshot = studentRef.get().await()
        val student = documentSnapshot.toObject<Student>()
        return student?.subjects
    }

    suspend fun searchStudent(query: String) {
        viewModelScope.launch {
            try {
                val resultList = mutableListOf<Student>()
                val nameQuery = db.collection("students").whereEqualTo("name", query).get().await()
                val rollNoQuery =
                    db.collection("students").whereEqualTo("rollNo", query).get().await()

                for (document in nameQuery.documents) {
                    val student = document.toObject<Student>()
                    if (student != null) {
                        resultList.add(student)
                    }
                }

                for (document in rollNoQuery.documents) {
                    val student = document.toObject<Student>()
                    if (student != null && !resultList.contains(student)) {
                        resultList.add(student)
                    }
                }

                _studentList.postValue(resultList)
            } catch (e: Exception) {
                Log.e(TAG, "Error searching student", e)
            }
        }
    }

    suspend fun getSomeStudents() {
        try {
            val getsudentlist = mutableListOf<Student>()
            val nameQuery = db.collection("students").limit(10).get().await()

            for (document in nameQuery.documents) {
                val student = document.toObject<Student>()
                if (student != null) {
                    getsudentlist.add(student)
                }
            }
            _studentList.postValue(getsudentlist)
        } catch (e: Exception) {
            Log.e(TAG, "Error get some students", e)
        }
    }
}