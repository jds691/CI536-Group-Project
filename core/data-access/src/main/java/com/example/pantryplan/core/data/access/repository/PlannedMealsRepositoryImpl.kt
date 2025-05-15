package com.example.pantryplan.core.data.access.repository

import com.example.pantryplan.core.database.dao.PlannedMealsDao
import com.example.pantryplan.core.database.model.PlannedMeal
import com.example.pantryplan.core.database.model.asExternalModel
import com.example.pantryplan.core.models.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class PlannedMealsRepositoryImpl @Inject constructor(
    private val plannedMealsDao: PlannedMealsDao,
    private val recipesRepository: RecipeRepository
) : PlannedMealsRepository {
    override suspend fun getPlannedMealsForDate(date: LocalDate, limit: Int): Flow<List<Recipe>> {
        val plannedMealsCount = plannedMealsDao.getPlannedMealsCountForDate(date).first()

        if (plannedMealsCount < limit) {
            var itemsToAdd = limit - plannedMealsCount

            while (itemsToAdd != 0) {
                val randRecipe = recipesRepository.getRandomRecipe()

                plannedMealsDao.addPlannedMealForDate(
                    PlannedMeal(
                        recipeId = randRecipe.id,
                        date = date
                    )
                )

                itemsToAdd -= 1
            }
        }

        return plannedMealsDao.getMealsForDate(date, limit).map { list ->
            list.map { item ->
                item.recipe.asExternalModel()
            }
        }
    }
}