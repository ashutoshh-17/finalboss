#include <stdio.h>
#include <omp.h>

int main() {
    int arr[8] = {1,2,3,4,5,6,7,8};
    int n = 8;
    int total_sum = 0;

    #pragma omp parallel
    {
        int local_sum = 0;
        int id = omp_get_thread_num();
        int num_threads = omp_get_num_threads();

        int chunk = n / num_threads;
        int start = id * chunk;
        int end;

        // Last thread takes remaining elements
        if(id == num_threads - 1)
            end = n;
        else
            end = start + chunk;

        for(int i = start; i < end; i++) {
            local_sum += arr[i];
        }

        printf("Thread %d calculated partial sum = %d\n", id, local_sum);

        #pragma omp critical
        total_sum += local_sum;
    }

    printf("Final Total Sum = %d\n", total_sum);
    return 0;
}