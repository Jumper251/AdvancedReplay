/**
 * This file provides a native method to supplement the Fast implementation
 * of RSA's MD5 hash generator in Java JDK Beta-2 or higher.
 * This file is Copyright (c) 2002 - 2010 Timothy W Macinta.
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * <p>
 * See http://www.twmacinta.com/myjava/fast_md5.php for more information
 * on this package.
 * <p>
 * See the Ant build file for details on compiling this file.
 *
 * @author	Timothy W Macinta (twm@alum.mit.edu)
 */

//////////////////////////////////////////////
//
//

#include <jni.h>
#include <stdint.h>
#include "MD5.h"

/**  Determine endian-ness. **/
#ifdef __WIN32__
#include <sys/param.h>

#else
#ifdef __APPLE__
#include <machine/endian.h>

#else
#ifdef __FreeBSD__
#include <sys/endian.h>

#else
#include <endian.h>

#endif
#endif
#endif


#ifndef __LITTLE_ENDIAN
#define __LITTLE_ENDIAN LITTLE_ENDIAN
#endif
#ifndef __BYTE_ORDER
#define __BYTE_ORDER BYTE_ORDER
#endif


//
//
//////////////////////////////////////////////

#define ROTATE_LEFT_UINT32(i, shift_left) (((i) << (shift_left)) | ((i) >> (32 - (shift_left))))

JNIEXPORT void JNICALL
Java_com_twmacinta_util_MD5_Transform_1native
(JNIEnv *env, jobject obj, jintArray state, jbyteArray buffer, jint shift, jint length) {
  register uint32_t a;
  register uint32_t b;
  register uint32_t c;
  register uint32_t d;
  uint32_t a0, b0, c0, d0, i, buffer_index;
#if __BYTE_ORDER == __LITTLE_ENDIAN
  uint32_t *x;
#else
  uint32_t x[16];
#endif
  jbyte buffer_elem[length];

  /* Copy the state to local variables **/

  jint *state_elem = (*env)->GetIntArrayElements(env, state, 0);
  a0 = state_elem[0];
  b0 = state_elem[1];
  c0 = state_elem[2];
  d0 = state_elem[3];

  /* Copy the region to be hashed into memory on the stack **/

  (*env)->GetByteArrayRegion(env, buffer, shift, length, buffer_elem);

  /* Loop through the region to be hashed in chunks of 64 bytes **/

  buffer_index = 0;
  for (buffer_index = 0; buffer_index + 63 < length; buffer_index += 64) {

    /* Save a copy of the state before modifications */

    a = a0;
    b = b0;
    c = c0;
    d = d0;

    /* equivalent to Decode(buffer, shift, decode_buf); in the Java code */

#if __BYTE_ORDER == __LITTLE_ENDIAN
    x = (uint32_t *) (buffer_elem + buffer_index);
#else
    x[0] = ((int) (buffer_elem[buffer_index] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 1] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 2] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 3]) << 24);
    x[1] = ((int) (buffer_elem[buffer_index + 4] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 5] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 6] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 7]) << 24);
    x[2] = ((int) (buffer_elem[buffer_index + 8] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 9] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 10] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 11]) << 24);
    x[3] = ((int) (buffer_elem[buffer_index + 12] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 13] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 14] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 15]) << 24);
    x[4] = ((int) (buffer_elem[buffer_index + 16] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 17] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 18] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 19]) << 24);
    x[5] = ((int) (buffer_elem[buffer_index + 20] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 21] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 22] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 23]) << 24);
    x[6] = ((int) (buffer_elem[buffer_index + 24] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 25] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 26] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 27]) << 24);
    x[7] = ((int) (buffer_elem[buffer_index + 28] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 29] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 30] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 31]) << 24);
    x[8] = ((int) (buffer_elem[buffer_index + 32] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 33] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 34] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 35]) << 24);
    x[9] = ((int) (buffer_elem[buffer_index + 36] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 37] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 38] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 39]) << 24);
    x[10] = ((int) (buffer_elem[buffer_index + 40] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 41] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 42] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 43]) << 24);
    x[11] = ((int) (buffer_elem[buffer_index + 44] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 45] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 46] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 47]) << 24);
    x[12] = ((int) (buffer_elem[buffer_index + 48] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 49] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 50] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 51]) << 24);
    x[13] = ((int) (buffer_elem[buffer_index + 52] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 53] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 54] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 55]) << 24);
    x[14] = ((int) (buffer_elem[buffer_index + 56] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 57] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 58] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 59]) << 24);
    x[15] = ((int) (buffer_elem[buffer_index + 60] & 0xff)) |
      (((int) (buffer_elem[buffer_index + 61] & 0xff)) << 8) |
      (((int) (buffer_elem[buffer_index + 62] & 0xff)) << 16) |
      (((int)  buffer_elem[buffer_index + 63]) << 24);
#endif

    /* Round 1 */
    a += ((b & c) | (~b & d)) + x[ 0] + 0xd76aa478; /* 1 */
    a = ROTATE_LEFT_UINT32(a, 7) + b;
    d += ((a & b) | (~a & c)) + x[ 1] + 0xe8c7b756; /* 2 */
    d = ROTATE_LEFT_UINT32(d, 12) + a;
    c += ((d & a) | (~d & b)) + x[ 2] + 0x242070db; /* 3 */
    c = ROTATE_LEFT_UINT32(c, 17) + d;
    b += ((c & d) | (~c & a)) + x[ 3] + 0xc1bdceee; /* 4 */
    b = ROTATE_LEFT_UINT32(b, 22) + c;

    a += ((b & c) | (~b & d)) + x[ 4] + 0xf57c0faf; /* 5 */
    a = ROTATE_LEFT_UINT32(a, 7) + b;
    d += ((a & b) | (~a & c)) + x[ 5] + 0x4787c62a; /* 6 */
    d = ROTATE_LEFT_UINT32(d, 12) + a;
    c += ((d & a) | (~d & b)) + x[ 6] + 0xa8304613; /* 7 */
    c = ROTATE_LEFT_UINT32(c, 17) + d;
    b += ((c & d) | (~c & a)) + x[ 7] + 0xfd469501; /* 8 */
    b = ROTATE_LEFT_UINT32(b, 22) + c;

    a += ((b & c) | (~b & d)) + x[ 8] + 0x698098d8; /* 9 */
    a = ROTATE_LEFT_UINT32(a, 7) + b;
    d += ((a & b) | (~a & c)) + x[ 9] + 0x8b44f7af; /* 10 */
    d = ROTATE_LEFT_UINT32(d, 12) + a;
    c += ((d & a) | (~d & b)) + x[10] + 0xffff5bb1; /* 11 */
    c = ROTATE_LEFT_UINT32(c, 17) + d;
    b += ((c & d) | (~c & a)) + x[11] + 0x895cd7be; /* 12 */
    b = ROTATE_LEFT_UINT32(b, 22) + c;

    a += ((b & c) | (~b & d)) + x[12] + 0x6b901122; /* 13 */
    a = ROTATE_LEFT_UINT32(a, 7) + b;
    d += ((a & b) | (~a & c)) + x[13] + 0xfd987193; /* 14 */
    d = ROTATE_LEFT_UINT32(d, 12) + a;
    c += ((d & a) | (~d & b)) + x[14] + 0xa679438e; /* 15 */
    c = ROTATE_LEFT_UINT32(c, 17) + d;
    b += ((c & d) | (~c & a)) + x[15] + 0x49b40821; /* 16 */
    b = ROTATE_LEFT_UINT32(b, 22) + c;
    
    
    /* Round 2 */
    a += ((b & d) | (c & ~d)) + x[ 1] + 0xf61e2562; /* 17 */
    a = ROTATE_LEFT_UINT32(a, 5) + b;
    d += ((a & c) | (b & ~c)) + x[ 6] + 0xc040b340; /* 18 */
    d = ROTATE_LEFT_UINT32(d, 9) + a;
    c += ((d & b) | (a & ~b)) + x[11] + 0x265e5a51; /* 19 */
    c = ROTATE_LEFT_UINT32(c, 14) + d;
    b += ((c & a) | (d & ~a)) + x[ 0] + 0xe9b6c7aa; /* 20 */
    b = ROTATE_LEFT_UINT32(b, 20) + c;

    a += ((b & d) | (c & ~d)) + x[ 5] + 0xd62f105d; /* 21 */
    a = ROTATE_LEFT_UINT32(a, 5) + b;
    d += ((a & c) | (b & ~c)) + x[10] + 0x02441453; /* 22 */
    d = ROTATE_LEFT_UINT32(d, 9) + a;
    c += ((d & b) | (a & ~b)) + x[15] + 0xd8a1e681; /* 23 */
    c = ROTATE_LEFT_UINT32(c, 14) + d;
    b += ((c & a) | (d & ~a)) + x[ 4] + 0xe7d3fbc8; /* 24 */
    b = ROTATE_LEFT_UINT32(b, 20) + c;

    a += ((b & d) | (c & ~d)) + x[ 9] + 0x21e1cde6; /* 25 */
    a = ROTATE_LEFT_UINT32(a, 5) + b;
    d += ((a & c) | (b & ~c)) + x[14] + 0xc33707d6; /* 26 */
    d = ROTATE_LEFT_UINT32(d, 9) + a;
    c += ((d & b) | (a & ~b)) + x[ 3] + 0xf4d50d87; /* 27 */
    c = ROTATE_LEFT_UINT32(c, 14) + d;
    b += ((c & a) | (d & ~a)) + x[ 8] + 0x455a14ed; /* 28 */
    b = ROTATE_LEFT_UINT32(b, 20) + c;

    a += ((b & d) | (c & ~d)) + x[13] + 0xa9e3e905; /* 29 */
    a = ROTATE_LEFT_UINT32(a, 5) + b;
    d += ((a & c) | (b & ~c)) + x[ 2] + 0xfcefa3f8; /* 30 */
    d = ROTATE_LEFT_UINT32(d, 9) + a;
    c += ((d & b) | (a & ~b)) + x[ 7] + 0x676f02d9; /* 31 */
    c = ROTATE_LEFT_UINT32(c, 14) + d;
    b += ((c & a) | (d & ~a)) + x[12] + 0x8d2a4c8a; /* 32 */
    b = ROTATE_LEFT_UINT32(b, 20) + c;
    
    
    /* Round 3 */
    a += (b ^ c ^ d) + x[ 5] + 0xfffa3942;      /* 33 */
    a = ROTATE_LEFT_UINT32(a, 4) + b;
    d += (a ^ b ^ c) + x[ 8] + 0x8771f681;      /* 34 */
    d = ROTATE_LEFT_UINT32(d, 11) + a;
    c += (d ^ a ^ b) + x[11] + 0x6d9d6122;      /* 35 */
    c = ROTATE_LEFT_UINT32(c, 16) + d;
    b += (c ^ d ^ a) + x[14] + 0xfde5380c;      /* 36 */
    b = ROTATE_LEFT_UINT32(b, 23) + c;
    
    a += (b ^ c ^ d) + x[ 1] + 0xa4beea44;      /* 37 */
    a = ROTATE_LEFT_UINT32(a, 4) + b;
    d += (a ^ b ^ c) + x[ 4] + 0x4bdecfa9;      /* 38 */
    d = ROTATE_LEFT_UINT32(d, 11) + a;
    c += (d ^ a ^ b) + x[ 7] + 0xf6bb4b60;      /* 39 */
    c = ROTATE_LEFT_UINT32(c, 16) + d;
    b += (c ^ d ^ a) + x[10] + 0xbebfbc70;      /* 40 */
    b = ROTATE_LEFT_UINT32(b, 23) + c;
    
    a += (b ^ c ^ d) + x[13] + 0x289b7ec6;      /* 41 */
    a = ROTATE_LEFT_UINT32(a, 4) + b;
    d += (a ^ b ^ c) + x[ 0] + 0xeaa127fa;      /* 42 */
    d = ROTATE_LEFT_UINT32(d, 11) + a;
    c += (d ^ a ^ b) + x[ 3] + 0xd4ef3085;      /* 43 */
    c = ROTATE_LEFT_UINT32(c, 16) + d;
    b += (c ^ d ^ a) + x[ 6] + 0x04881d05;      /* 44 */
    b = ROTATE_LEFT_UINT32(b, 23) + c;
    
    a += (b ^ c ^ d) + x[ 9] + 0xd9d4d039;      /* 33 */
    a = ROTATE_LEFT_UINT32(a, 4) + b;
    d += (a ^ b ^ c) + x[12] + 0xe6db99e5;      /* 34 */
    d = ROTATE_LEFT_UINT32(d, 11) + a;
    c += (d ^ a ^ b) + x[15] + 0x1fa27cf8;      /* 35 */
    c = ROTATE_LEFT_UINT32(c, 16) + d;
    b += (c ^ d ^ a) + x[ 2] + 0xc4ac5665;      /* 36 */
    b = ROTATE_LEFT_UINT32(b, 23) + c;
    

    /* Round 4 */
    a += (c ^ (b | ~d)) + x[ 0] + 0xf4292244; /* 49 */
    a = ROTATE_LEFT_UINT32(a, 6) + b;
    d += (b ^ (a | ~c)) + x[ 7] + 0x432aff97; /* 50 */
    d = ROTATE_LEFT_UINT32(d, 10) + a;
    c += (a ^ (d | ~b)) + x[14] + 0xab9423a7; /* 51 */
    c = ROTATE_LEFT_UINT32(c, 15) + d;
    b += (d ^ (c | ~a)) + x[ 5] + 0xfc93a039; /* 52 */
    b = ROTATE_LEFT_UINT32(b, 21) + c;

    a += (c ^ (b | ~d)) + x[12] + 0x655b59c3; /* 53 */
    a = ROTATE_LEFT_UINT32(a, 6) + b;
    d += (b ^ (a | ~c)) + x[ 3] + 0x8f0ccc92; /* 54 */
    d = ROTATE_LEFT_UINT32(d, 10) + a;
    c += (a ^ (d | ~b)) + x[10] + 0xffeff47d; /* 55 */
    c = ROTATE_LEFT_UINT32(c, 15) + d;
    b += (d ^ (c | ~a)) + x[ 1] + 0x85845dd1; /* 56 */
    b = ROTATE_LEFT_UINT32(b, 21) + c;

    a += (c ^ (b | ~d)) + x[ 8] + 0x6fa87e4f; /* 57 */
    a = ROTATE_LEFT_UINT32(a, 6) + b;
    d += (b ^ (a | ~c)) + x[15] + 0xfe2ce6e0; /* 58 */
    d = ROTATE_LEFT_UINT32(d, 10) + a;
    c += (a ^ (d | ~b)) + x[ 6] + 0xa3014314; /* 59 */
    c = ROTATE_LEFT_UINT32(c, 15) + d;
    b += (d ^ (c | ~a)) + x[13] + 0x4e0811a1; /* 60 */
    b = ROTATE_LEFT_UINT32(b, 21) + c;

    a += (c ^ (b | ~d)) + x[ 4] + 0xf7537e82; /* 61 */
    a = ROTATE_LEFT_UINT32(a, 6) + b;
    d += (b ^ (a | ~c)) + x[11] + 0xbd3af235; /* 62 */
    d = ROTATE_LEFT_UINT32(d, 10) + a;
    c += (a ^ (d | ~b)) + x[ 2] + 0x2ad7d2bb; /* 63 */
    c = ROTATE_LEFT_UINT32(c, 15) + d;
    b += (d ^ (c | ~a)) + x[ 9] + 0xeb86d391; /* 64 */
    b = ROTATE_LEFT_UINT32(b, 21) + c;

    /* update the state */
    
    a0 += a;
    b0 += b;
    c0 += c;
    d0 += d;
  }

  /* update the state array */
  
  state_elem[0] = a0;
  state_elem[1] = b0;
  state_elem[2] = c0;
  state_elem[3] = d0;
  (*env)->ReleaseIntArrayElements(env, state, state_elem, 0);
}
