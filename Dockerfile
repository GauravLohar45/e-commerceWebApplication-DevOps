# Stage-1: Build Stage
FROM nginx:alpine as build
WORKDIR /usr/share/nginx/html
COPY index.html .

# Stage-2: Final Image
FROM nginx:alpine
WORKDIR /usr/share/nginx/html
COPY --from=build /usr/share/nginx/html .
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
