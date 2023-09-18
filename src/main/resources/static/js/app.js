new Vue({
    el: '#app',
    data: {
        user: {
            username: '',
            email: '',
            password: ''
        }
    },
    methods: {
        registerUser() {
            // Prepare the user data for the POST request
            const userData = {
                username: this.user.username,
                email: this.user.email,
                password: this.user.password
            };

            // Make a POST request to your backend registration endpoint
            axios.post('http://127.0.0.1:8080/api/v1/auth/register', userData)
                .then(response => {
                    // Handle the response from the backend
                    console.log('Registration successful:', response.data);
                    alert('User registered successfully!');
                })
                .catch(error => {
                    // Handle any errors that occur during the POST request
                    console.error('Error registering user:', error);
                    alert('An error occurred while registering the user.');
                });
        }
    }
});