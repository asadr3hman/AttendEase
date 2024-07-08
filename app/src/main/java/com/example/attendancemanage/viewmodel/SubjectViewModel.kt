package com.example.attendancemanage.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendancemanage.model.Subject

class SubjectViewModel : ViewModel() {
    private val _selectedSubjects = mutableStateListOf<Subject>()
    val selectedSubjects: List<Subject> get() = _selectedSubjects

    fun addSubject(subject: Subject) {
        if (_selectedSubjects.none { it.subjectTitle == subject.subjectTitle }) {
            _selectedSubjects.add(subject)
        }
    }

    fun removeSubject(subject: Subject) {
        _selectedSubjects.remove(subject)
    }
}
