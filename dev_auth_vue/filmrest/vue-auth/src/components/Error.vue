<template>
  <div class="alert alert-danger p-1" role="alert">
    {{error}}
  </div>
</template>

<script>
export default {
  name: 'Error',
  props: ['error']
}

</script>