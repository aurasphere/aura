package co.aurasphere.aura.nebula.modules.dashboard.view.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.aurasphere.aura.nebula.dashboard.common.entities.Project;
import co.aurasphere.aura.nebula.dashboard.common.entities.Todo;
import co.aurasphere.aura.nebula.dashboard.common.enumeration.ProjectCategory;
import co.aurasphere.aura.nebula.dashboard.common.enumeration.TaskPriority;
import co.aurasphere.aura.nebula.modules.dashboard.controller.DashboardService;
import co.aurasphere.aura.nebula.modules.dashboard.ioc.DaggerDashboardComponent;
import co.aurasphere.aura.nebula.modules.dashboard.ioc.DashboardComponent;
import co.aurasphere.aura.nebula.modules.dashboard.model.DashboardTaskType;
import co.aurasphere.aura.nebula.R;

/**
 * Created by Donato on 21/05/2016.
 */
public class DashboardAddNewTaskActivity extends AppCompatActivity {

    @Inject
    DashboardService dashboardController;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.dashboard_add_task_form_type_spinner)
    Spinner taskTypeSpinner;

    @BindView(R.id.dashboard_add_task_form_project_layout)
    View addProjectLayout;

    @BindView(R.id.dashboard_add_task_form_deadline_edit_text)
    EditText taskDeadlineEditText;

    @BindView(R.id.dashboard_add_task_form_description_edit_text)
    EditText taskDescription;

    @BindView(R.id.dashboard_add_task_form_priority_spinner)
    Spinner taskPrioritySpinner;

    @BindView(R.id.dashboard_add_task_form_project_category_spinner)
    Spinner projectCategorySpinner;

    @BindView(R.id.dashboard_add_task_form_create_button)
    Button createButton;

    private Calendar taskDeadline = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Dependency injection.
        DashboardComponent dashboardComponent = DaggerDashboardComponent.builder().build();
        dashboardComponent.inject(this);

        // View bindings.
        setContentView(R.layout.toolbar_main_layout);
        final View content = View.inflate(this, R.layout.dashboard_add_task_form_layout, null);
        ViewGroup frameLayout = (ViewGroup) findViewById(R.id.main_content);
        frameLayout.addView(content);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Fills in the spinners.
        taskTypeSpinner.setAdapter(new ArrayAdapter<DashboardTaskType>(this, android.R.layout.simple_list_item_1, DashboardTaskType.values()));
        taskTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Switches views according to what has been selected.
                if (DashboardTaskType.PROJECT.ordinal() == id) {
                    addProjectLayout.setVisibility(View.VISIBLE);
                } else {
                    addProjectLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Never happens in this case.
            }
        });

        taskPrioritySpinner.setAdapter(new ArrayAdapter<TaskPriority>(this, android.R.layout.simple_list_item_1, TaskPriority.values()));
        projectCategorySpinner.setAdapter(new ArrayAdapter<ProjectCategory>(this, android.R.layout.simple_list_item_1, ProjectCategory.values()));
    }

    @OnClick(R.id.dashboard_add_task_form_deadline_edit_text)
    public void openPopupCalendar(){
        // Sets the date picker popup on the date edit text.
        DatePickerDialog  mdiDialog = new DatePickerDialog(DashboardAddNewTaskActivity.this ,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                taskDeadline.set(Calendar.YEAR, year);
                taskDeadline.set(Calendar.MONTH, monthOfYear);
                taskDeadline.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String dateString =  String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear + 1)+ "/" + year;
                taskDeadlineEditText.setText(dateString);
            }
        }, taskDeadline.get(Calendar.YEAR), taskDeadline.get(Calendar.MONTH), taskDeadline.get(Calendar.DAY_OF_MONTH));
        mdiDialog.show();
    }

    @OnClick(R.id.dashboard_add_task_form_create_button)
    public void createTask(){
        // Disables the button
        createButton.setEnabled(false);
        // Creates the corresponded object and sends it.
        DashboardTaskType taskType = (DashboardTaskType) taskTypeSpinner.getSelectedItem();
        switch(taskType){
            case TODO:
                createTodo();
                break;
            case PROJECT:
                createProject();
                break;
        }
    }

    private void createTodo() {
        Todo todo = new Todo();
        todo.setDescription(taskDescription.getText().toString());
        todo.setDeadline(taskDeadline);
        todo.setPriority((TaskPriority) taskPrioritySpinner.getSelectedItem());
        dashboardController.addTodo(todo, this);
    }

    private void createProject() {
        Project project = new Project();
        project.setDescription(taskDescription.getText().toString());
        project.setDeadline(taskDeadline);
        project.setPriority((TaskPriority) taskPrioritySpinner.getSelectedItem());
        project.setCategory((ProjectCategory) projectCategorySpinner.getSelectedItem());
        dashboardController.addProject(project, this);
    }


    public void onAddTaskComplete(boolean outcome) {
        String message;
        if(outcome){
            message = "Succesfully created Todo.";
        } else {
            message = "Error during Todo creation.";
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        createButton.setEnabled(true);
    }
}
