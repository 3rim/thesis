<template>
  <Suspense>
    <AsyncUsersView />
    <template #fallback>
      <p>loading</p>
    </template>
  </Suspense>
</template>

<script setup>
import axios from 'axios' ;
import { ref } from 'vue';
import authHeader from '../services/authHeader';
import AsyncUsersView from '../components/AsyncUsersView.vue'

</script>
