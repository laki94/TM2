package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryStudentGradeModel
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList

@Dao
interface LaboratoryStudentGradeDao {
    @Insert
    fun insert(laboratoryStudentGrade: LaboratoryStudentGradeModel):Long

    @Update
    fun update(laboratoryStudentGrade: LaboratoryStudentGradeModel)

    @Delete
    fun delete(laboratoryStudentGrade: LaboratoryStudentGradeModel)

    @Query("SELECT * FROM laboratorystudentgrademodel WHERE laboratory_id = :laboratoryId AND student_id = :studentId")
    fun getItemForStudentAtLaboratory(laboratoryId: Int, studentId: Int): LaboratoryStudentGradeModel?
}